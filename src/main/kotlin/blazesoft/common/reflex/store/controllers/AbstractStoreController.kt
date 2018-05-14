package blazesoft.common.reflex.store.controllers

import blazesoft.common.reflex.store.model.Store
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

    @GetMapping("")
    fun getInitialStore(): Mono<Store<TState>> {
        log.debug("getInitialStore")
        return Mono.just(storeService.createStore())
    }

    @GetMapping("/{storeId}")
    fun getStore(@PathVariable storeId: String): Mono<Store<TState>> {
        log.debug("getStore")
        return Mono.justOrEmpty(storeService.getStore(storeId))
    }

    @GetMapping("/{storeId}/actions")
    fun getStoreActions(@PathVariable storeId: String): Flux<StoreAction<TState>>  {
        log.debug("getStoreActions")
        return storeService.getStore(storeId)!!.getActionsBySource(AbstractStoreAction.BACKEND_SOURCE)
    }

    @PostMapping("/{storeId}/actions")
    fun dispatch(@PathVariable storeId: String, @RequestBody action: AbstractStoreAction<TState>) {
        log.debug("dispatch $action")
        if (action.source != AbstractStoreAction.BACKEND_SOURCE) {
            storeService.getStore(storeId)!!.dispatch(action)
        }
    }

    companion object {
        private val log = LogFactory.getLog(AbstractStoreController::class.java)
    }
}
