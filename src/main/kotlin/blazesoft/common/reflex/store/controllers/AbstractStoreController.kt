package blazesoft.common.reflex.store.controllers

import blazesoft.common.reflex.store.exceptions.BadActionException
import blazesoft.common.reflex.store.model.actions.AbstractStoreAction
import blazesoft.common.reflex.store.model.actions.StoreAction
import blazesoft.common.reflex.store.model.state.State
import blazesoft.common.reflex.store.services.AbstractStoreService
import org.apache.commons.logging.LogFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

abstract class AbstractStoreController<TState : State>(private val storeService: AbstractStoreService<TState>) {


    @GetMapping("/state")
    fun getInitialState(): Mono<TState> {
        log.debug("getInitialState")
        return storeService.initialStore
                .flatMap{ it.state }
    }

    @GetMapping("/{stateId}/state")
    fun getState(@PathVariable stateId: String): Mono<TState> {
        log.debug("getState")
        return storeService.getStore(stateId)
                .flatMap { it.state }
    }

    @GetMapping("/{stateId}/actions")
    fun getStoreActions(@PathVariable stateId: String): Flux<StoreAction<TState>>  {
        log.debug("getStoreActions")
        return storeService.getStore(stateId)
                .flatMapMany { s -> s.getOuterActionsBySource(AbstractStoreAction.BACKEND_SOURCE) }
    }



    @PostMapping("/{stateId}/actions")
    fun dispatch(@PathVariable stateId: String,
                 @RequestBody action: AbstractStoreAction<TState>) {
        log.debug("dispatch $action")
        if (action.source == AbstractStoreAction.BACKEND_SOURCE) {
            throw BadActionException(action, "Cannot process backend actions")
        }

        storeService.getStore(stateId)
                .subscribe { s -> s.dispatch(action) }
    }

    companion object {
        private val log = LogFactory.getLog(AbstractStoreController::class.java)
    }
}
