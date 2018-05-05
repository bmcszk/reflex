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
import javax.annotation.PostConstruct

abstract class AbstractStoreConfigurer(private val scannedPackage: String) {

    @Autowired
    private val mapper: ObjectMapper? = null

    @PostConstruct
    private fun init() {
        val scanner = ClassPathScanningCandidateComponentProvider(false)

        scanner.addIncludeFilter(AnnotationTypeFilter(StoreAction::class.java))

        val subtypes = scanner.findCandidateComponents(scannedPackage)
                .map { bd -> NamedType(getClassForName(bd.beanClassName), getStoreActionType(bd)) }
                .toTypedArray()

        mapper!!.registerSubtypes(*subtypes)

        if (log.isDebugEnabled) {
            val subtypeStrings = subtypes
                    .map { s -> s.name }
                    .joinToString { ", " }
            log.debug(String.format("Registered store action subtypes: [%s]", subtypeStrings))
        }
    }

    private fun getClassForName(name: String?): Class<*> {
        try {
            return Class.forName(name)
        } catch (e: Exception) {
            log.error(e)
            throw RuntimeException(e)
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
