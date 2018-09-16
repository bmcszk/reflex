package blazesoft.common.reflex.store

import blazesoft.common.reflex.store.services.AbstractStoreService
import org.springframework.stereotype.Service

@Service
class PingPongService: AbstractStoreService<PingPongState>() {
    override fun createInitialState(): PingPongState {
        return PingPongState("initial")
    }
}