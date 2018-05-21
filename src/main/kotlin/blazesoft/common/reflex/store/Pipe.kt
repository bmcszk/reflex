package blazesoft.common.reflex.store

import org.apache.commons.logging.LogFactory
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink

class Pipe<T> {

    private val processor: EmitterProcessor<T> = EmitterProcessor.create()
    private val sink: FluxSink<T> = processor.sink()

    val flux: Flux<T>
        get() = Flux.from(processor)

    val isAvailable: Boolean
        get() = !(processor.isDisposed && processor.isTerminated)

    fun sink(data: T) {
        try {
            sink.next(data)
        } catch (ex: Exception) {
            log.error(ex)
        }

    }

    companion object {
        private val log = LogFactory.getLog(Pipe::class.java)
    }
}