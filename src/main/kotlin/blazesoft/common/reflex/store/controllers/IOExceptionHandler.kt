package blazesoft.common.reflex.store.controllers

import org.apache.commons.logging.LogFactory
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono
import java.io.IOException

@ControllerAdvice
class IOExceptionHandler : WebExceptionHandler {

    @ExceptionHandler(IOException::class)
    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        return Mono.justOrEmpty(ex.message)
                .doOnNext { msg -> log.warn("IOException occurred: $msg") }
                .then()
    }

    companion object {
        private val log = LogFactory.getLog(IOExceptionHandler::class.java)
    }
}
