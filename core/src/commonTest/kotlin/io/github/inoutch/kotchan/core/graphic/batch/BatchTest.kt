package io.github.inoutch.kotchan.core.graphic.batch

import io.github.inoutch.kotchan.core.KotchanGlobalContext
import io.github.inoutch.kotchan.core.KotchanPlatformLauncher
import io.github.inoutch.kotchan.core.KotchanStartupConfig
import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.core.graphic.polygon.Sprite
import io.github.inoutch.kotchan.core.view.scene.Scene
import io.github.inoutch.kotchan.core.view.scene.SceneContext
import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F
import io.github.inoutch.kotchan.math.flatten
import io.github.inoutch.kotchan.test.utillity.logger.TestLogger
import io.github.inoutch.kotchan.test.utillity.mock.MockContext
import io.github.inoutch.kotchan.test.utillity.mock.MockMaterial
import kotlin.test.Test

class BatchTest {
    data class MockSprites(
            val sprite1: Sprite = Sprite(Vector2F(10, 10)),
            val sprite2: Sprite = Sprite(Vector2F(10, 10)),
            val sprite3: Sprite = Sprite(Vector2F(10, 10)),
            val baseVertices: List<Vector3F> = Sprite.createSquarePositions(Vector2F.Zero, Vector2F(10.0f, 10.0f)),
            val baseTexcoords: List<Vector2F> = Sprite.createSquareTexcoords(Vector2F.Zero, Vector2F(1.0f, 1.0f))
    ) {
        fun transformPositions(sprite: Sprite, position: Vector3F): Array<Float> {
            sprite.position = position
            sprite.anchorPoint = Vector2F.Zero
            return baseVertices.map { it + position }.flatten().toTypedArray()
        }

        fun transformColors(sprite: Sprite, color: Vector4F): Array<Float> {
            sprite.color = color
            return baseVertices.map { color }.flatten().toTypedArray()
        }
    }

    fun init(): TestLogger {
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
        return testLogger
    }

    @Test
    fun checkBatchCreation() {
        val testLogger = init()

        Batch(MockMaterial(testLogger))
        testLogger.assertEquals(listOf(
                "createVertexBuffer:float: 1000, Dynamic, 1000",
                "createVertexBuffer:float: 1000, Dynamic, 1000",
                "createVertexBuffer:float: 1000, Dynamic, 1000"
        ))
        testLogger.clear()
    }

    @Test
    fun checkFirstRenderingAndNoChanges() {
        val testLogger = init()

        val batch = Batch(MockMaterial(testLogger))
        testLogger.clear()

        val mock = MockSprites()
        batch.add(mock.sprite1)
        val s1v = mock.transformPositions(mock.sprite1, Vector3F(10.0f, 33.0f, -23.0f))
        val s2v = mock.transformPositions(mock.sprite2, Vector3F(40.0f, 37.0f, 94.0f))
        val s3v = mock.transformPositions(mock.sprite3, Vector3F(880.0f, -12.0f, -240.0f))

        batch.add(mock.sprite2)

        val s1c = mock.transformColors(mock.sprite1, Vector4F(1.0f, 0.0f, 0.0f, 1.0f))
        val s2c = mock.transformColors(mock.sprite2, Vector4F(1.0f, 1.0f, 0.0f, 1.0f))
        val s3c = mock.transformColors(mock.sprite3, Vector4F(1.0f, 1.0f, 1.0f, 1.0f))
        batch.add(mock.sprite3)

        batch.render()
        val st = mock.baseTexcoords.flatten().toTypedArray()
        testLogger.assertEquals(listOf(
                "bind",
                "copyToBuffer: 0, 54, ${listOf(*s1v, *s2v, *s3v)}",
                "copyToBuffer: 0, 72, ${listOf(*s1c, *s2c, *s3c)}",
                "copyToBuffer: 0, 36, ${listOf(*st, *st, *st)}",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()
    }

    @Test
    fun checkChangePositions() {
        val testLogger = init()

        val batch = Batch(MockMaterial(testLogger))
        testLogger.clear()

        val mock = MockSprites()
        batch.add(mock.sprite1)
        var s1v = mock.transformPositions(mock.sprite1, Vector3F(10.0f, 33.0f, -23.0f))
        var s2v = mock.transformPositions(mock.sprite2, Vector3F(40.0f, 37.0f, 94.0f))
        var s3v = mock.transformPositions(mock.sprite3, Vector3F(880.0f, -12.0f, -240.0f))

        batch.add(mock.sprite2)

        var s1c = mock.transformColors(mock.sprite1, Vector4F(1.0f, 0.0f, 0.0f, 1.0f))
        var s2c = mock.transformColors(mock.sprite2, Vector4F(1.0f, 1.0f, 0.0f, 1.0f))
        var s3c = mock.transformColors(mock.sprite3, Vector4F(1.0f, 1.0f, 1.0f, 1.0f))
        batch.add(mock.sprite3)

        val st = mock.baseTexcoords.flatten().toTypedArray()
        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "copyToBuffer: 0, 54, ${listOf(*s1v, *s2v, *s3v)}",
                "copyToBuffer: 0, 72, ${listOf(*s1c, *s2c, *s3c)}",
                "copyToBuffer: 0, 36, ${listOf(*st, *st, *st)}",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        s1v = mock.transformPositions(mock.sprite1, Vector3F(23.0f, -44.0f, 19999.0f))
        s2v = mock.transformPositions(mock.sprite2, Vector3F(337626.0f, 4537.820f, 85654.0f))
        s3v = mock.transformPositions(mock.sprite3, Vector3F(372.73740f, 1837.46f, 274676599.0f))

        s1c = mock.transformColors(mock.sprite1, Vector4F(0.0f, 1.0f, 1.0f, 1.0f))
        s2c = mock.transformColors(mock.sprite2, Vector4F(1.0f, 0.0f, 1.0f, 1.0f))
        s3c = mock.transformColors(mock.sprite3, Vector4F(0.0f, 0.0f, 0.0f, 0.0f))

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "copyToBuffer: 0, 54, ${listOf(*s1v, *s2v, *s3v)}",
                "copyToBuffer: 0, 72, ${listOf(*s1c, *s2c, *s3c)}",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        s1v = mock.transformPositions(mock.sprite1, Vector3F(-384.596866f, 9284.0f, 489009.056f))
        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "copyToBuffer: 0, ${s1v.size}, ${listOf(*s1v)}",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        s2v = mock.transformPositions(mock.sprite2, Vector3F(-384.596866f, 9284.0f, 489009.056f))
        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "copyToBuffer: 18, ${s2v.size}, ${listOf(*s2v)}",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        s1v = mock.transformPositions(mock.sprite1, Vector3F(0.1f, 0.2f, 0.3f))
        s3v = mock.transformPositions(mock.sprite3, Vector3F(0.4f, 0.5f, 0.6f))

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "copyToBuffer: 0, 54, ${listOf(*s1v, *s2v, *s3v)}",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()
    }

    @Test
    fun checkChangeColors() {
        val testLogger = init()

        val batch = Batch(MockMaterial(testLogger))
        testLogger.clear()

        val mock = MockSprites()
        batch.add(mock.sprite1)
        val s1v = mock.transformPositions(mock.sprite1, Vector3F(10.0f, 33.0f, -23.0f))
        val s2v = mock.transformPositions(mock.sprite2, Vector3F(40.0f, 37.0f, 94.0f))
        val s3v = mock.transformPositions(mock.sprite3, Vector3F(880.0f, -12.0f, -240.0f))

        batch.add(mock.sprite2)

        var s1c = mock.transformColors(mock.sprite1, Vector4F(1.0f, 0.0f, 0.0f, 1.0f))
        var s2c = mock.transformColors(mock.sprite2, Vector4F(1.0f, 1.0f, 0.0f, 1.0f))
        var s3c = mock.transformColors(mock.sprite3, Vector4F(1.0f, 1.0f, 1.0f, 1.0f))
        batch.add(mock.sprite3)

        val st = mock.baseTexcoords.flatten().toTypedArray()
        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "copyToBuffer: 0, 54, ${listOf(*s1v, *s2v, *s3v)}",
                "copyToBuffer: 0, 72, ${listOf(*s1c, *s2c, *s3c)}",
                "copyToBuffer: 0, 36, ${listOf(*st, *st, *st)}",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        s1c = mock.transformColors(mock.sprite1, Vector4F(0.0f, 1.0f, 1.0f, 1.0f))
        s2c = mock.transformColors(mock.sprite2, Vector4F(1.0f, 0.0f, 1.0f, 1.0f))
        s3c = mock.transformColors(mock.sprite3, Vector4F(0.0f, 0.0f, 0.0f, 0.0f))

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "copyToBuffer: 0, 72, ${listOf(*s1c, *s2c, *s3c)}",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        s1c = mock.transformColors(mock.sprite1, Vector4F(1.0f, 1.0f, 1.0f, 1.0f))
        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "copyToBuffer: 0, ${s1c.size}, ${listOf(*s1c)}",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        s2c = mock.transformColors(mock.sprite2, Vector4F(0.2345f, 1.0f, 0.455f, 0.6543f))
        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "copyToBuffer: 24, ${s2c.size}, ${listOf(*s2c)}",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()

        s1c = mock.transformColors(mock.sprite1, Vector4F(0.1f, 0.2f, 0.3f, 0.4f))
        s3c = mock.transformColors(mock.sprite3, Vector4F(1.0f, 0.9f, 0.8f, 0.7f))

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "copyToBuffer: 0, 72, ${listOf(*s1c, *s2c, *s3c)}",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()
    }

    @Test
    fun checkIncrementalAdding() {
        val testLogger = init()
        val batch = Batch(MockMaterial(testLogger))
        testLogger.clear()

        val mock = MockSprites()
        batch.add(mock.sprite1)
        var s1v = mock.transformPositions(mock.sprite1, Vector3F(10.0f, 33.0f, -23.0f))
        val s1c = mock.transformColors(mock.sprite1, Vector4F(1.0f, 0.0f, 0.0f, 1.0f))
        val st = mock.baseTexcoords.flatten().toTypedArray()

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "copyToBuffer: 0, 18, ${listOf(*s1v)}",
                "copyToBuffer: 0, 24, ${listOf(*s1c)}",
                "copyToBuffer: 0, 12, ${listOf(*st)}",
                "drawTriangles: 3, 6"
        ))
        testLogger.clear()

        var s2v = mock.transformPositions(mock.sprite2, Vector3F(40.0f, 37.0f, 94.0f))
        var s2c = mock.transformColors(mock.sprite2, Vector4F(1.0f, 1.0f, 0.0f, 1.0f))

        batch.add(mock.sprite2)
        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "copyToBuffer: 18, 18, ${listOf(*s2v)}",
                "copyToBuffer: 24, 24, ${listOf(*s2c)}",
                "copyToBuffer: 12, 12, ${listOf(*st)}",
                "drawTriangles: 3, 12"
        ))
        testLogger.clear()

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "drawTriangles: 3, 12"
        ))
        testLogger.clear()

        val s3v = mock.transformPositions(mock.sprite3, Vector3F(880.0f, -12.0f, -240.0f))
        val s3c = mock.transformColors(mock.sprite3, Vector4F(1.0f, 1.0f, 1.0f, 1.0f))

        s1v = mock.transformPositions(mock.sprite1, Vector3F(45.01f, 23.0f, -48.37464f))
        batch.add(mock.sprite3)

        s2v = mock.transformPositions(mock.sprite2, Vector3F(453.01f, 234.0f, -48.375464f))
        s2c = mock.transformColors(mock.sprite2, Vector4F(0.98f, 0.34f, 0.12f, 1.0f))

        batch.render()
        testLogger.assertEquals(listOf(
                "bind",
                "copyToBuffer: 0, 54, ${listOf(*s1v, *s2v, *s3v)}",
                "copyToBuffer: 24, 48, ${listOf(*s2c, *s3c)}",
                "copyToBuffer: 24, 12, ${listOf(*st)}",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()
    }

    @Test
    fun checkRemove() {
        val testLogger = init()

        val batch = Batch(MockMaterial(testLogger))
        testLogger.clear()

        val mock = MockSprites()
        batch.add(mock.sprite1)
        val s1v = mock.transformPositions(mock.sprite1, Vector3F(10.0f, 33.0f, -23.0f))
        val s2v = mock.transformPositions(mock.sprite2, Vector3F(40.0f, 37.0f, 94.0f))
        val s3v = mock.transformPositions(mock.sprite3, Vector3F(880.0f, -12.0f, -240.0f))

        batch.add(mock.sprite2)

        val s1c = mock.transformColors(mock.sprite1, Vector4F(1.0f, 0.0f, 0.0f, 1.0f))
        val s2c = mock.transformColors(mock.sprite2, Vector4F(1.0f, 1.0f, 0.0f, 1.0f))
        val s3c = mock.transformColors(mock.sprite3, Vector4F(1.0f, 1.0f, 1.0f, 1.0f))
        batch.add(mock.sprite3)

        batch.render()
        val st = mock.baseTexcoords.flatten().toTypedArray()
        testLogger.assertEquals(listOf(
                "bind",
                "copyToBuffer: 0, 54, ${listOf(*s1v, *s2v, *s3v)}",
                "copyToBuffer: 0, 72, ${listOf(*s1c, *s2c, *s3c)}",
                "copyToBuffer: 0, 36, ${listOf(*st, *st, *st)}",
                "drawTriangles: 3, 18"
        ))
        testLogger.clear()
    }
}
