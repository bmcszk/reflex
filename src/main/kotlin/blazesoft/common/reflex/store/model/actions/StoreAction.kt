package blazesoft.common.reflex.store.model.actions

import blazesoft.common.reflex.store.model.state.State
import java.util.*

interface StoreAction<TState : State> {
    val scopes: Array<String>
    val date: Date
    fun reduce(stateData: TState): TState
}
