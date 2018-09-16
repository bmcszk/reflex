package blazesoft.common.reflex.store.actions

import blazesoft.common.reflex.store.State
import java.util.*

interface StoreAction<TState : State> {
    val scopes: Array<String>
    val date: Date
    fun reduce(state: TState): TState
}
