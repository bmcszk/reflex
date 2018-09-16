package blazesoft.common.reflex.store.controllers

import blazesoft.common.reflex.store.State
import blazesoft.common.reflex.store.actions.AbstractStoreAction
import blazesoft.common.reflex.store.actions.StoreAction
import blazesoft.common.reflex.store.services.AbstractStoreService
import org.apache.commons.logging.LogFactory
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

abstract class AbstractStoreController<TState : State>(
        private val storeService: AbstractStoreService<TState>
) {

    //@GetMapping("/{id}/actions", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    open fun getStoreActions(@PathVariable id: String, scope: String): Flux<StoreAction<TState>>  {
        log.debug("getStoreActions")

        return storeService.getStore(id)
                .getActions(scope)
    }

    @PostMapping("/{id}/actions")
    fun dispatch(@PathVariable id: String,
                 @RequestBody action: AbstractStoreAction<TState>) : Mono<Void> {
        log.debug("dispatch $action")

        storeService.getStore(id)
                .dispatch(action)

        return Mono.empty()
    }

    companion object {
        private val log = LogFactory.getLog(AbstractStoreController::class.java)
    }
}
