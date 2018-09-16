package blazesoft.common.reflex.store.handlers

import blazesoft.common.reflex.store.State
import blazesoft.common.reflex.store.Store
import blazesoft.common.reflex.store.actions.FetchStateAction
import blazesoft.common.reflex.store.actions.FetchStateSuccessAction

class FetchStateActionHandler<TState : State, TAction: FetchStateAction<TState>> : AbstractActionHandler<TState, TAction>() {
    override val scope: String
        get() = "private"

    override fun handleAction(store: Store<TState>, action: TAction) {
        log.debug("handleAction $action")
        store.dispatch(
                FetchStateSuccessAction(
                        arrayOf("private", "server"),
                        store.state))
    }

}