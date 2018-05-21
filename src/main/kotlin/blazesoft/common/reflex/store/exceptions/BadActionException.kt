package blazesoft.common.reflex.store.exceptions

import blazesoft.common.reflex.store.model.actions.StoreAction
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadActionException(action: StoreAction<*>, message: String)
    : RuntimeException("Bad action $action: $message")
