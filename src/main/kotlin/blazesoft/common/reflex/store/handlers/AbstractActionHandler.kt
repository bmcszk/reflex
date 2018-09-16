package blazesoft.common.reflex.store.handlers

import blazesoft.common.reflex.store.State
import blazesoft.common.reflex.store.Store
import blazesoft.common.reflex.store.actions.StoreAction
import blazesoft.common.reflex.store.services.AbstractStoreService
import com.google.common.reflect.TypeToken
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct

abstract class AbstractActionHandler<TState : State, TAction : StoreAction<TState>> {
    abstract val scope: String

    protected val log = LogFactory.getLog(this.javaClass)!!

    private val actionTypeToken = object : TypeToken<TAction>(javaClass) {

    }
    private val tAction = (actionTypeToken.type as Class<TAction>).kotlin

    @Autowired
    private lateinit var storeService: AbstractStoreService<TState>

    @PostConstruct
    private fun init() {
        log.debug("init")
        storeService.storeFlux
                .subscribe { handleStore(it) }
    }


    private fun handleStore(store: Store<TState>) {
        log.debug("handleStore")
        store.getActionsByType(tAction, scope)
                .subscribe { handleAction(store, it as TAction) }
    }

    protected abstract fun handleAction(store: Store<TState>, action: TAction)
}
