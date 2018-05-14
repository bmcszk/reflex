package blazesoft.common.reflex.store

import blazesoft.common.reflex.store.model.Store
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
class StoreTest {

    private lateinit var store: Store<PingPongState>

    @Autowired
    private lateinit var service: PingPongService

    @Before
    fun setUp() {
        store = service.createStore()
    }


    @Test
    fun shouldPingPong() {
        store.dispatch(PingAction("front", Date()))

        StepVerifier.create(store.getActions())
                .expectNextMatches({ it is PingAction })
                .expectNextMatches({ it is PongAction })

        Assertions.assertThat(store.state.block()!!.status).isEqualTo("pong")
    }

    @Test
    fun shouldGetActionsByType() {
        store.dispatch(PingAction("front", Date()))
        store.dispatch(PingAction("front", Date()))

        StepVerifier.create(store.getActionsByType(PongAction::class))
                .expectNextMatches({ it is PongAction })
                .expectNextMatches({ it is PongAction })

        Assertions.assertThat(store.state.block()!!.status).isEqualTo("pong")
    }

}