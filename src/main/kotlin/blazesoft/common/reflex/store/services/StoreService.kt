package blazesoft.common.reflex.store.services

import blazesoft.common.reflex.store.model.Store
import blazesoft.common.reflex.store.model.state.State
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.ReplayProcessor

import java.util.HashMap

@Service
class StoreService<TState : State> {

    private val stores = HashMap<String, Store<TState>>()
    private val storeFlux = ReplayProcessor.create<Store<TState>>()
    private val storeFluxSink = storeFlux.sink()

    fun getStore(storeId: String): Store<TState>? {
        return stores[storeId]
    }

    fun createStore(): Store<TState> {
        val store = Store<TState>()
        stores[store.id] = store
        storeFluxSink.next(store)
        return store
    }

    fun getStoreFlux(): Flux<Store<TState>> {
        return storeFlux
    }

    companion object {
        private val log = LogFactory.getLog(StoreService::class.java)
    }
}
