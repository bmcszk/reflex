package blazesoft.common.reflex.store

import blazesoft.common.reflex.store.handlers.AbstractActionHandler
import org.springframework.stereotype.Component
import java.util.*

@Component
class PingActionHandler
    : AbstractActionHandler<PingPongState, PingAction>() {
    override val scope: String
        get() = "public"


    override fun handleAction(store: Store<PingPongState>, action: PingAction) {
        log.debug("handleAction $action")
        store.dispatch(PongAction(
                arrayOf("server", "public"),
                Date()))
    }

}