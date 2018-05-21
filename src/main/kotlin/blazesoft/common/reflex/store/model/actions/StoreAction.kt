package blazesoft.common.reflex.store.model.actions

import blazesoft.common.reflex.store.model.state.State
import reactor.core.publisher.Mono
import java.util.*

interface StoreAction<TState : State> {
    val source: String
    val date: Date
    fun reduce(state: TState): Mono<TState>
}
