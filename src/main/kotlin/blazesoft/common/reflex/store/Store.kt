package blazesoft.common.reflex.store

import blazesoft.common.reflex.store.model.actions.StoreAction
import blazesoft.common.reflex.store.model.state.State
import org.apache.commons.logging.LogFactory
import reactor.core.publisher.Flux
import kotlin.reflect.KClass

class Store<TState : State>
constructor (
        id: String,
        var state: TState) {

    private val actionPipes = Pipes<StoreAction<TState>>()
    private val actionPipe = actionPipes.registerPipe()

    @Synchronized
    fun dispatch(action: StoreAction<TState>) {
        log.debug("dispatch $action")
        state = action.reduce(state)
        actionPipes.sink(action)
    }

    fun <T : StoreAction<TState>> getActionsByType(type: KClass<T>, scope: String): Flux<T> {
        return actionPipe.flux
                .filter {
                    type.isInstance(it) && it.scopes.contains(scope)
                }
                .map { it as T }
    }

    fun getActions(scope: String): Flux<StoreAction<TState>> {
        return actionPipe.flux
                .filter{ it.scopes.contains(scope)}
    }

    companion object {
        private val log = LogFactory.getLog(Store::class.java)
    }

}