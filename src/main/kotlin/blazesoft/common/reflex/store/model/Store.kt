package blazesoft.common.reflex.store.model

import blazesoft.common.reflex.store.model.actions.StoreAction
import blazesoft.common.reflex.store.model.state.State
import com.fasterxml.jackson.annotation.JsonIgnore
import org.apache.commons.logging.LogFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.ReplayProcessor
import java.util.*
import kotlin.reflect.KClass

class Store<TState : State> {
    private val actions = ReplayProcessor.create<StoreAction<TState>>()
    private val actionFluxSink = actions.sink()
    var state = Mono.empty<TState>()
    val id = UUID.randomUUID().toString()

    @Synchronized
    fun dispatch(action: StoreAction<TState>) {
        log.debug("dispatch $action")
        state = action.reduce(state)
        actionFluxSink.next(action)
    }

    @JsonIgnore
    fun getActions(): Flux<StoreAction<TState>> {
        return actions
    }

    @JsonIgnore
    fun <TAction : StoreAction<TState>> getActionsByType(type : KClass<TAction>): Flux<TAction> {
        return getActions()
                .filter { type.isInstance(it) }
                .map { it as TAction }
    }

    @JsonIgnore
    fun getActionsBySource(source: String): Flux<StoreAction<TState>> {
        return actions
                .filter { a -> a.source == source }
    }

    companion object {
        private val log = LogFactory.getLog(Store.javaClass)
    }

}
