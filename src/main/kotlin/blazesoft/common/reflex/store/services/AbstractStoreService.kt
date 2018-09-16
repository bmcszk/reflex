package blazesoft.common.reflex.store.services

import blazesoft.common.reflex.store.Pipe
import blazesoft.common.reflex.store.Store
import blazesoft.common.reflex.store.model.state.State
import org.apache.commons.logging.LogFactory
import reactor.core.publisher.Flux
import java.util.concurrent.ConcurrentHashMap

abstract class AbstractStoreService<TState : State> {

    private val storePipe = Pipe<Store<TState>>()

    private val storeMap = ConcurrentHashMap<String, Store<TState>>()

    val storeFlux: Flux<Store<TState>>
        get() = storePipe.flux

    fun getStore(id: String): Store<TState> {
        log.debug("getStore")
        return storeMap[id]
            ?: createStore(id, createInitialState())
    }

    private fun createStore(id : String, state: TState): Store<TState> {
        log.debug("createStore")
        val store = Store(id, state)
        storeMap[id] = store
        storePipe.sink(store)

        return store
    }

    abstract fun createInitialState(): TState

    companion object {
        private val log = LogFactory.getLog(AbstractStoreService::class.java)
    }
}