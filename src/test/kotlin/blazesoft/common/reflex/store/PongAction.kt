package blazesoft.common.reflex.store

import blazesoft.common.reflex.store.annotations.StoreAction
import blazesoft.common.reflex.store.model.actions.AbstractStoreAction
import reactor.core.publisher.Mono
import java.util.*

@StoreAction("PONG")
data class PongAction(override val source: String, override val date: Date) : AbstractStoreAction<PingPongState>(source, date) {
    override fun reduce(state: Mono<PingPongState>): Mono<PingPongState> {
        return Mono.just(PingPongState(null, "pong"))
    }
}