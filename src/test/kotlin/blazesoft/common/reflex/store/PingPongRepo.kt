package blazesoft.common.reflex.store

import blazesoft.common.reflex.store.model.state.StateWrapper
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface PingPongRepo: ReactiveCrudRepository<StateWrapper<PingPongState>, String>