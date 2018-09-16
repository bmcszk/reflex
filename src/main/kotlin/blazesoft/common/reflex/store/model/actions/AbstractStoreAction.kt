package blazesoft.common.reflex.store.model.actions

import blazesoft.common.reflex.store.model.state.State
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
abstract class AbstractStoreAction<T : State> (
        override val scopes: Array<String>,
        override val date: Date = Date()) : StoreAction<T> {

    override fun toString(): String {
        return this.javaClass.simpleName
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractStoreAction<*>) return false

        if (!Arrays.equals(scopes, other.scopes)) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(scopes)
        result = 31 * result + date.hashCode()
        return result
    }


}
