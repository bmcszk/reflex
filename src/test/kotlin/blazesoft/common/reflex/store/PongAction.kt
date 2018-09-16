package blazesoft.common.reflex.store

import blazesoft.common.reflex.store.actions.AbstractStoreAction
import blazesoft.common.reflex.store.annotations.StoreAction
import java.util.*

@StoreAction("PONG")
data class PongAction(
        override val scopes: Array<String>,
        override val date: Date)
    : AbstractStoreAction<PingPongState>(scopes, date) {
    override fun reduce(state: PingPongState): PingPongState {
        return PingPongState( "pong")
    }
}