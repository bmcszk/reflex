package blazesoft.common.reflex.store.model.state

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "states")
data class StateWrapper<TState : State>(
        @Id val id: String?,
        val state: TState)