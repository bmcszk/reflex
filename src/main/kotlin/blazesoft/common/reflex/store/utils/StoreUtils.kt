package blazesoft.common.reflex.store.utils

import com.google.common.collect.ImmutableList
import org.springframework.beans.BeanUtils
import org.springframework.beans.BeanWrapperImpl
import java.util.stream.Stream

object StoreUtils {
    fun <T> objectAssign(target: T?, vararg objs: Any): T? {
        if (target == null) {
            return null
        }
        Stream.of(*objs)
                .filter { o -> o != null }
                .forEach { o -> BeanUtils.copyProperties(o, target, *getNullPropertyNames(o)) }

        return target
    }

    fun <T> buildImmutableList(list: List<T>?, item: T): List<T> {
        return if (list != null)
            ImmutableList.Builder<T>()
                    .addAll(list)
                    .add(item)
                    .build()
        else
            ImmutableList.of(item)
    }

    private fun getNullPropertyNames(source: Any): Array<String> {
        val wrappedSource = BeanWrapperImpl(source)
        return wrappedSource.propertyDescriptors
                .map { it.name }
                //.map<String>(Function<PropertyDescriptor, String> { it.getName() })
                .filter { propertyName -> wrappedSource.getPropertyValue(propertyName) == null }
                .toTypedArray()
                //.toArray(String[]::new  /* Currently unsupported in Kotlin */)
    }
}
