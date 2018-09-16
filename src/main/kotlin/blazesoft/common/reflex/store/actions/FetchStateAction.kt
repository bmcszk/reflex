package blazesoft.common.reflex.store.actions

import blazesoft.common.reflex.store.State
import blazesoft.common.reflex.store.annotations.StoreAction

@StoreAction("FETCH_STATE")
data class FetchStateAction<TState : State>(
        override val scopes: Array<String>
): AbstractStoreAction<TState>(scopes) {

    override fun reduce(state: TState): TState {
        return state
    }
}