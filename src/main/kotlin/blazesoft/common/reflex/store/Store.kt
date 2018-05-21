package blazesoft.common.reflex.store

import blazesoft.common.reflex.store.model.actions.StoreAction
import blazesoft.common.reflex.store.model.state.State
import blazesoft.common.reflex.store.model.state.StateWrapper
import org.apache.commons.logging.LogFactory
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.reflect.KClass

class Store<TState : State>
constructor (
        private val repo: ReactiveCrudRepository<StateWrapper<TState>, String>,
        stateWrapper: StateWrapper<TState>) {

    val stateId: String? = stateWrapper.id
    private var stateWrapperPublisher: Mono<StateWrapper<TState>> = Mono.just(stateWrapper)
    private val actionPipes = Pipes<StoreAction<TState>>()
    private val innerActionPipe = actionPipes.registerPipe()
    private val outerActionPipe = actionPipes.registerPipe()

    val state : Mono<TState>
            get() = stateWrapperPublisher.map { it.state }

    @Synchronized
    fun dispatch(action: StoreAction<TState>) {
        log.debug("dispatch $action")
        stateWrapperPublisher = stateWrapperPublisher
                .flatMap { action.reduce(it.state) }
                .map { StateWrapper(stateId, it) }
                .flatMap { repo.save(it) }
        actionPipes.sink(action)
    }

    fun <T : StoreAction<TState>> getInnerActionsByType(type: KClass<T>): Flux<T> {
        return innerActionPipe.flux
                .filter { type.isInstance(it) }
                .map { it as T }
    }

    fun getOuterActionsBySource(source: String): Flux<StoreAction<TState>> {
        return outerActionPipe.flux
                .filter{ it.source == source }
    }

    companion object {
        private val log = LogFactory.getLog(Store::class.java)
    }

}