package blazesoft.common.reflex.store

import blazesoft.common.reflex.store.annotations.StoreAction
import blazesoft.common.reflex.store.model.actions.AbstractStoreAction
import java.util.*

@StoreAction("PONG")
data class PongAction(
        override val scopes: Array<String>,
        override val date: Date)
    : AbstractStoreAction<PingPongState>(scopes, date) {
    override fun reduce(stateData: PingPongState): PingPongState {
        return PingPongState( "pong")
    }
}