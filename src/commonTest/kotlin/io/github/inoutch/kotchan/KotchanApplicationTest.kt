package io.github.inoutch.kotchan

import kotlinx.coroutines.yield

class KotchanApplicationTest {
    suspend fun launch() {
        println("hello world!")
        yield()
        println("hello world!")
    }
}
