package blazesoft.common.reflex.store

import blazesoft.common.reflex.store.annotations.StoreAction
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.NamedType
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.util.ClassUtils
import javax.annotation.PostConstruct

abstract class AbstractStoreConfigurer(private val scannedPackage: String) {

    @Autowired
    private lateinit var mapper: ObjectMapper

    @PostConstruct
    private fun init() {
        val scanner = ClassPathScanningCandidateComponentProvider(false)

        scanner.addIncludeFilter(AnnotationTypeFilter(StoreAction::class.java))

        val subtypes = scanner.findCandidateComponents(scannedPackage)
                .filter { it.beanClassName != null }
                .map {  NamedType(ClassUtils.forName(it.beanClassName!!, ClassUtils.getDefaultClassLoader()), getStoreActionType(it)) }
                .toTypedArray()

        mapper.registerSubtypes(*subtypes)

        if (log.isDebugEnabled) {
            val subtypeStrings = subtypes
                    .map { s -> s.name }
                    .joinToString(", ")
            log.debug("Registered store action subtypes: [$subtypeStrings]")
        }
    }

    private fun getStoreActionType(beanDefinition: BeanDefinition): String? {
        if (beanDefinition is AnnotatedBeanDefinition) {
            val attributes = beanDefinition.metadata.getAnnotationAttributes(StoreAction::class.java.name)

            return attributes!!["value"].toString()
        }
        return null
    }

    companion object {
        private val log = LogFactory.getLog(AbstractStoreConfigurer::class.java)
    }
}
