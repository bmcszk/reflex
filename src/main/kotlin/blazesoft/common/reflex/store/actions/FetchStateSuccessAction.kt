package blazesoft.common.reflex.store.actions

import blazesoft.common.reflex.store.State
import blazesoft.common.reflex.store.annotations.StoreAction

@StoreAction("FETCH_STATE_SUCCESS")
data class FetchStateSuccessAction<TState : State>(
        override val scopes: Array<String>,
        val state: TState
) : AbstractStoreAction<TState>(scopes) {

    override fun reduce(state: TState): TState {
        return state
    }
}