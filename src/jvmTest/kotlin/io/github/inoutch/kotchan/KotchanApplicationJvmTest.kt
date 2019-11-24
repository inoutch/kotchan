package io.github.inoutch.kotchan

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runBlockingTest

@ExperimentalCoroutinesApi
class KotchanApplicationJvmTest {
    private val commonTest = KotchanApplicationTest()

    fun sample() {
        println("sample")
    }

    @Test
    fun launch() = runBlockingTest {
        async { commonTest.launch() }
        sample()
        assertFalse { false }
    }
}
