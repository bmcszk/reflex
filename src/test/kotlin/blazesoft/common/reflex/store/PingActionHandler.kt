package blazesoft.common.reflex.store

import blazesoft.common.reflex.store.handlers.AbstractActionHandler
import blazesoft.common.reflex.store.model.Store
import blazesoft.common.reflex.store.model.actions.AbstractStoreAction
import org.springframework.stereotype.Component
import java.util.*

@Component
class PingActionHandler : AbstractActionHandler<PingPongState, PingAction>() {
    override fun handleAction(store: Store<PingPongState>, action: PingAction) {
        log.debug("handleAction $action")
        store.dispatch(PongAction(
                AbstractStoreAction.BACKEND_SOURCE,
                Date()))
    }

}