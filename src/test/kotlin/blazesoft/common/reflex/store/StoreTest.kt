package blazesoft.common.reflex.store

import blazesoft.common.reflex.store.model.actions.AbstractStoreAction
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration
import java.util.*

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
class StoreTest {

    private lateinit var store: Store<PingPongState>

    @Autowired
    private lateinit var service: PingPongService

    @Before
    fun setUp() {
        store = service.initialStore
                .block()!!
    }


    @Test
    fun shouldPingPong() {
        Mono.just(store)
                .delayElement(Duration.ofSeconds(3))
                .subscribe { it.dispatch(PingAction("front", Date())) }

        StepVerifier.create(store.getOuterActionsBySource(AbstractStoreAction.BACKEND_SOURCE))
                .expectNextMatches({ it is PongAction })
                .thenCancel()
                .verify()

        Assertions.assertThat(store.state.block()!!.status).isEqualTo("pong")
    }

    @Test
    fun shouldGetActionsByType() {
        Mono.just(store)
                .delayElement(Duration.ofSeconds(3))
                .subscribe {
            it.dispatch(PingAction("front", Date()))
            it.dispatch(PingAction("front", Date()))
        }

        StepVerifier.create(store.getInnerActionsByType(PongAction::class))
                .expectNextMatches({ it is PongAction })
                .expectNextMatches({ it is PongAction })
                .thenCancel()
                .verify()

        Assertions.assertThat(store.state.block()!!.status).isEqualTo("pong")
    }

}