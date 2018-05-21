package blazesoft.common.reflex.store

import java.util.*

class Pipes<T> {
    private val pipeList = ArrayList<Pipe<T>>()

    fun registerPipe(): Pipe<T> {
        val pipe = Pipe<T>()
        pipeList.add(pipe)
        return pipe
    }

    fun sink(data: T) {
        pipeList
                .stream()
                .filter { p -> p.isAvailable }
                .forEach { p -> p.sink(data) }
    }


}