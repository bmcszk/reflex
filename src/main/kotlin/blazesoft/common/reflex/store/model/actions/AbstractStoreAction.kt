package blazesoft.common.reflex.store.model.actions

import blazesoft.common.reflex.store.model.state.State
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
abstract class AbstractStoreAction<T : State> (override val source: String) : StoreAction<T> {

    override fun toString(): String {
        return this.javaClass.simpleName
    }

    companion object {
        val BACKEND_SOURCE = "backend"
    }

}
