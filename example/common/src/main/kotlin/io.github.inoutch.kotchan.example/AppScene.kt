package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.error.NoSuchFileError
import io.github.inoutch.kotchan.core.graphic.Scene
import io.github.inoutch.kotchan.core.graphic.VertexBuffer
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipeline
import io.github.inoutch.kotchan.core.graphic.shader.SimpleShaderProgram
import io.github.inoutch.kotchan.utility.graphic.Image
import io.github.inoutch.kotchan.utility.graphic.loadFromResource
import io.github.inoutch.kotchan.utility.graphic.vulkan.*
import io.github.inoutch.kotchan.utility.io.readBytesFromResourceWithError
import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.Vector3
import io.github.inoutch.kotchan.utility.type.Vector4

class AppScene : Scene() {

    private lateinit var pos: VertexBuffer
    private lateinit var col: VertexBuffer
    private lateinit var tex: VertexBuffer
    private val graphicsPipeline: GraphicsPipeline

    private val path = instance.file.getResourcePath("tiles/sample.png")
            ?: throw NoSuchFileError("sprites/spritesheet.png")
    private val image = instance.graphicsApi.loadTexture(path)

    private val shaderProgram: SimpleShaderProgram = SimpleShaderProgram()

    private val camera = instance.createCamera2D()

    init {
        val createInfo = GraphicsPipeline.CreateInfo(shaderProgram)
        graphicsPipeline = instance.graphicsApi.createGraphicsPipeline(createInfo)

        val vk = instance.vk
        if (vk != null) {
            pos = VertexBuffer(vk, floatArrayOf(
                    0.0f, 0.0f, 0.0f,
                    200.0f, 0.0f, 0.0f,
                    0.0f, 200.0f, 0.0f))
            col = VertexBuffer(vk, floatArrayOf(
                    1.0f, 1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f, 1.0f))
            tex = VertexBuffer(vk, floatArrayOf(
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    0.0f, 0.0f))
        }
    }

    override fun draw(delta: Float) {
        camera.position -= Vector3(delta, 0.0f, 0.0f)
        camera.update()

        val vk = instance.vk ?: return

        val extent = vk.swapchainRecreator.extent

        val renderPassBeginInfo = VkRenderPassBeginInfo(
                vk.renderPass,
                vk.currentFrameBuffer,
                VkRect2D(Point.ZERO, extent),
                listOf(VkClearValue(Vector4(0.0f, 1.0f, 0.0f, 1.0f)),
                        VkClearValue(VkClearDepthStencilValue(0.0f, 0))))

        shaderProgram.update(delta, camera)
        instance.graphicsApi.setSampler(shaderProgram.sampler, image)
        instance.graphicsApi.bindGraphicsPipeline(graphicsPipeline)
        instance.graphicsApi.setViewport(instance.viewport)

        vkCmdBindVertexBuffers(
                vk.currentCommandBuffer,
                0,
                listOf(pos.buffer, col.buffer, tex.buffer),
                listOf(0, 0, 0))

        vkCmdBeginRenderPass(vk.currentCommandBuffer, renderPassBeginInfo, VkSubpassContents.VK_SUBPASS_CONTENTS_INLINE)

        vkCmdDraw(vk.currentCommandBuffer, 3, 1, 0, 0)

        vkCmdEndRenderPass(vk.currentCommandBuffer)
    }

    override fun pause() {}

    override fun resume() {}

    override fun reshape(x: Int, y: Int, width: Int, height: Int) {}

    override fun destroyed() {}
}
