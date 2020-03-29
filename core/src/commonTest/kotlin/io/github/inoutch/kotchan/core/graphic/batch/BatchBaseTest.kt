package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.KotchanGlobalContext
import io.github.inoutch.kotchan.core.KotchanPlatformLauncher
import io.github.inoutch.kotchan.core.KotchanStartupConfig
import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.core.view.scene.Scene
import io.github.inoutch.kotchan.core.view.scene.SceneContext
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.test.utillity.logger.TestLogger
import io.github.inoutch.kotchan.test.utillity.mock.MockBatchBuffer
import io.github.inoutch.kotchan.test.utillity.mock.MockContext
import io.github.inoutch.kotchan.test.utillity.mock.MockMaterial
import kotlin.test.BeforeTest
import kotlin.test.Test

class BatchBaseTest {
    @BeforeTest
    fun before() {
        val testLogger = TestLogger()
        KotchanGlobalContext().initialize(object : KotchanStartupConfig() {
            override val applicationName: String
                get() = "test"
            override val windowSize: Vector2I = Vector2I.Zero
            override val screenSize: Vector2I = Vector2I.Zero
            override fun createFirstScene(context: SceneContext): Scene {
                throw Error()
            }
        }, object : KotchanPlatformLauncher {
            override fun getGraphics(): Context {
                return MockContext(testLogger)
            }

            override suspend fun startAnimation() {
            }
        })
    }

    @Test
    fun checkAdd() {
        val testLogger = TestLogger()
        val batch = object : BatchBase<Int>(MockMaterial(testLogger)) {
            override val bundles: List<Bundle> = listOf(Bundle(3, MockBatchBuffer(testLogger)))

            override fun update(objectBundle: BatchObjectBufferBundle<Int>) {
                testLogger.log(this, "update: ${objectBundle.obj}, ${objectBundle.index}, ${objectBundle.pointers[0].first}, ${objectBundle.pointers[0].size}")
            }

            override fun size(obj: Int): Int {
                testLogger.log(this, "size: $obj")
                return obj
            }
        }

        testLogger.assertEquals(emptyList())
        batch.add(10)

        testLogger.assertEquals(listOf("size: 10", "allocate: 30"))
        testLogger.clear()

        batch.add(30)
        batch.add(20)
        batch.add(90)
        testLogger.assertEquals(listOf("size: 30", "allocate: 90", "size: 20", "allocate: 60", "size: 90", "allocate: 270"))
    }

    @Test
    fun checkRender() {
        val testLogger = TestLogger()
        val batch = object : BatchBase<Int>(MockMaterial(testLogger)) {
            override val bundles: List<Bundle> = listOf(Bundle(3, MockBatchBuffer(testLogger)))

            override fun update(objectBundle: BatchObjectBufferBundle<Int>) {
                testLogger.log(this, "update: ${objectBundle.obj}, ${objectBundle.index}, ${objectBundle.pointers[0].first}, ${objectBundle.pointers[0].size}")
            }

            override fun size(obj: Int): Int {
                testLogger.log(this, "size: $obj")
                return obj
            }
        }

        batch.add(10)
        batch.render()

        testLogger.assertEquals(listOf("size: 10", "allocate: 30", "bind", "update: 10, 0, 0, 30", "flush"))
        testLogger.clear()

        batch.add(30)
        batch.add(50, 2)
        batch.add(45, 5)
        testLogger.clear()

        batch.render()
        testLogger.assertEquals(listOf("bind", "update: 10, 0, 0, 30", "update: 30, 0, 30, 90", "update: 50, 2, 120, 150", "update: 45, 5, 270, 135", "flush"))
    }

    @Test
    fun checkRemove() {
        val testLogger = TestLogger()
        val batch = object : BatchBase<Int>(MockMaterial(testLogger)) {
            override val bundles: List<Bundle> = listOf(Bundle(3, MockBatchBuffer(testLogger)))

            override fun update(objectBundle: BatchObjectBufferBundle<Int>) {
                testLogger.log(this, "update: ${objectBundle.obj}, ${objectBundle.index}, ${objectBundle.pointers[0].first}, ${objectBundle.pointers[0].size}")
            }

            override fun size(obj: Int): Int {
                testLogger.log(this, "size: $obj")
                return obj
            }
        }

        batch.add(10)
        testLogger.clear()

        batch.remove(10)
        testLogger.assertEquals(listOf("free: 0, 30"))
        testLogger.clear()

        batch.add(10)
        batch.add(55)
        batch.add(27)
        testLogger.clear()

        batch.remove(55)
        testLogger.assertEquals(listOf("free: 30, 165"))
        testLogger.clear()

        batch.render()
        testLogger.assertEquals(listOf("bind", "update: 10, 0, 0, 30", "update: 27, 0, 30, 81", "flush", "copyToBuffer: 0, 111, ${List(111) { 0.0f }}"))
        testLogger.clear()

        batch.remove(27)
        testLogger.assertEquals(listOf("free: 30, 81"))
        testLogger.clear()

        batch.remove(10)
        testLogger.assertEquals(listOf("free: 0, 30"))
        testLogger.clear()

        batch.render()
        testLogger.assertEquals(listOf("bind", "flush"))
    }

    @Test
    fun checkSort() {
        val testLogger = TestLogger()
        val batch = object : BatchBase<Int>(MockMaterial(testLogger)) {
            override val bundles: List<Bundle> = listOf(Bundle(3, MockBatchBuffer(testLogger)))

            override fun update(objectBundle: BatchObjectBufferBundle<Int>) {
                testLogger.log(this, "update: ${objectBundle.obj}, ${objectBundle.index}, ${objectBundle.pointers[0].first}, ${objectBundle.pointers[0].size}")
            }

            override fun size(obj: Int): Int {
                testLogger.log(this, "size: $obj")
                return obj
            }
        }
        batch.add(3, 3) // 0:9
        batch.add(4, 4) // 9:12
        batch.add(2, 2) // 21:6
        batch.add(5, 5) // 27:15
        batch.add(1, 1) // 42:3
        testLogger.clear()

        batch.sortByRenderOrder()
        testLogger.assertEquals(listOf(
                "sort",
                "adder: 42, 3",
                "adder: 21, 6",
                "adder: 0, 9",
                "adder: 9, 12",
                "adder: 27, 15"
        ))
        testLogger.clear()

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "update: 1, 1, 0, 3",
                "update: 2, 2, 3, 6",
                "update: 3, 3, 9, 9",
                "update: 4, 4, 18, 12",
                "update: 5, 5, 30, 15",
                "flush",
                "copyToBuffer: 0, 45, ${List(45) { 0.0f }}"
        ))
    }
}
