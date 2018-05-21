package blazesoft.common.reflex.store.services

import blazesoft.common.reflex.store.Pipe
import blazesoft.common.reflex.store.Store
import blazesoft.common.reflex.store.model.state.State
import blazesoft.common.reflex.store.model.state.StateWrapper
import org.apache.commons.logging.LogFactory
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap

abstract class AbstractStoreService<TState : State> (
        private val stateRepo: ReactiveCrudRepository<StateWrapper<TState>, String>) {

    private val storePipe = Pipe<Store<TState>>()

    private val storeMap = ConcurrentHashMap<String, Store<TState>>()

    val initialStore: Mono<Store<TState>>
        get() {
            log.debug("getInitialStore")
            return stateRepo.save(createInitialStateWrapper())
                    .map { createStore(it) }
        }

    val storeFlux: Flux<Store<TState>>
        get() = storePipe.flux

    fun getStore(stateId: String): Mono<Store<TState>> {
        log.debug("getStore")
        val store = storeMap[stateId] ?: return stateRepo.findById(stateId)
                .map { createStore(it) }
                .switchIfEmpty(initialStore)
        return Mono.just(store)
    }

    private fun createStore(stateWrapper: StateWrapper<TState>): Store<TState> {
        log.debug("createStore")
        val store = Store(stateRepo, stateWrapper)
        if (stateWrapper.id != null) {
            storeMap[stateWrapper.id] = store
        }
        storePipe.sink(store)

        return store
    }

    private fun createInitialStateWrapper(): StateWrapper<TState> {
        return StateWrapper(null, createInitialState())
    }

    protected abstract fun createInitialState(): TState

    companion object {
        private val log = LogFactory.getLog(AbstractStoreService::class.java)
    }
}