package blazesoft.common.reflex.store.model

import blazesoft.common.reflex.store.model.actions.StoreAction
import blazesoft.common.reflex.store.model.state.State
import com.fasterxml.jackson.annotation.JsonIgnore
import org.apache.commons.logging.LogFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.ReplayProcessor

import java.lang.reflect.Type
import java.util.UUID

class Store<TState : State> {
    private val log = LogFactory.getLog(this.javaClass)

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
    fun <T : StoreAction<TState>> getActionsByType(type: Type): Flux<T> {
        return getActionsByType(type as Class<T>)
    }

    @JsonIgnore
    fun <T : StoreAction<TState>> getActionsByType(type: Class<T>): Flux<T> {
        return actions
                .filter { a -> type.isInstance(a) } as Flux<T>
    }

    @JsonIgnore
    fun getActionsBySource(source: String): Flux<StoreAction<TState>> {
        return actions
                .filter { a -> a.source == source }
    }

    companion object {

    }

}
