package blazesoft.common.reflex.store

import blazesoft.common.reflex.store.services.AbstractStoreService
import org.springframework.stereotype.Service

@Service
class PingPongService(stateRepo: PingPongRepo) : AbstractStoreService<PingPongState>(stateRepo) {
    override fun createInitialState(): PingPongState {
        return PingPongState("initial")
    }
}