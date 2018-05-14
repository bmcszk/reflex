package blazesoft.common.reflex.store

import  blazesoft.common.reflex.store.model.state.State

data class PingPongState(override var id: String?, val status: String) : State
