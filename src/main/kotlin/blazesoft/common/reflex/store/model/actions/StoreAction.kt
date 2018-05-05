package blazesoft.common.reflex.store.model.actions

import blazesoft.common.reflex.store.model.state.State
import reactor.core.publisher.Mono

interface StoreAction<TState : State> {
    val source: String
    fun reduce(state: Mono<TState>): Mono<TState>
}
