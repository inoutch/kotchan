package io.github.inoutch.kotchan.core.graphic.compatible.vk

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.graphic.batch.BatchBufferBundle
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipeline
import io.github.inoutch.kotchan.core.graphic.compatible.GraphicsPipelineConfig
import io.github.inoutch.kotchan.core.graphic.compatible.Image
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.BufferStorageMode
import io.github.inoutch.kotchan.core.graphic.compatible.buffer.VertexBuffer
import io.github.inoutch.kotchan.core.graphic.compatible.context.Context
import io.github.inoutch.kotchan.core.graphic.compatible.shader.Shader
import io.github.inoutch.kotchan.core.graphic.compatible.shader.ShaderProgram
import io.github.inoutch.kotchan.core.graphic.compatible.shader.ShaderSource
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform1F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform1I
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform2F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform3F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.Uniform4F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformMatrix4F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformTexture
import io.github.inoutch.kotchan.extension.getProperties
import io.github.inoutch.kotchan.extension.getProperty
import io.github.inoutch.kotchan.math.RectI
import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotchan.math.Vector4F
import io.github.inoutch.kotlin.vulkan.api.VkClearColorValue
import io.github.inoutch.kotlin.vulkan.api.VkClearDepthStencilValue
import io.github.inoutch.kotlin.vulkan.api.VkClearValue
import io.github.inoutch.kotlin.vulkan.api.VkCommandBufferBeginInfo
import io.github.inoutch.kotlin.vulkan.api.VkCommandBufferUsageFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkExtent2D
import io.github.inoutch.kotlin.vulkan.api.VkImageAspectFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkImageLayout.VK_IMAGE_LAYOUT_GENERAL
import io.github.inoutch.kotlin.vulkan.api.VkImageLayout.VK_IMAGE_LAYOUT_PRESENT_SRC_KHR
import io.github.inoutch.kotlin.vulkan.api.VkImageLayout.VK_IMAGE_LAYOUT_UNDEFINED
import io.github.inoutch.kotlin.vulkan.api.VkImageSubresourceRange
import io.github.inoutch.kotlin.vulkan.api.VkInstance
import io.github.inoutch.kotlin.vulkan.api.VkInstanceCreateInfo
import io.github.inoutch.kotlin.vulkan.api.VkOffset2D
import io.github.inoutch.kotlin.vulkan.api.VkPhysicalDevice
import io.github.inoutch.kotlin.vulkan.api.VkPipelineStageFlagBits
import io.github.inoutch.kotlin.vulkan.api.VkPresentInfoKHR
import io.github.inoutch.kotlin.vulkan.api.VkRect2D
import io.github.inoutch.kotlin.vulkan.api.VkResult
import io.github.inoutch.kotlin.vulkan.api.VkStructureType
import io.github.inoutch.kotlin.vulkan.api.VkSubmitInfo
import io.github.inoutch.kotlin.vulkan.api.VkSurface
import io.github.inoutch.kotlin.vulkan.api.VkViewport
import io.github.inoutch.kotlin.vulkan.api.VkWriteDescriptorSet
import io.github.inoutch.kotlin.vulkan.api.vk
import io.github.inoutch.kotlin.vulkan.utility.MutableProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.math.min

class VKContext(
    instanceCreateInfo: VkInstanceCreateInfo,
    private var windowSize: Vector2I,
    maxFrameInFlight: Int = 3,
    createSurface: (surface: MutableProperty<VkSurface>, instance: VkInstance) -> VkResult
) : Context, Disposer() {

    val instance: VkInstance

    val surface: VkSurface

    val physicalDevices: List<VKPhysicalDevice>

    val primaryPhysicalDevice: VKPhysicalDevice

    val primaryLogicalDevice: VKLogicalDevice

    val primaryGraphicCommandPool: VKCommandPool

    val swapchainRecreator: VKSwapchainRecreator

    var currentFrame = 0
        private set

    var currentSwapchainImageIndex = 0
        private set

    val currentSwapchainImageIndexManager = VKSwapchainImageIndexManagerImpl()

    private val imageAvailableSemaphores: List<VKSemaphore>

    private val renderCompleteSemaphores: List<VKSemaphore>

    private val inFlightFences: List<VKFence>

    private var mustRecreateSwapchainInFrame = false

    private var currentRenderContext: VKRenderContext

    private var currentGraphicsPipeline: VKGraphicsPipeline? = null

    private val emptyTexture: Texture

    init {
        try {
            instance = getProperty { vk.createInstance(instanceCreateInfo, it).value }
            add { vk.destroyInstance(instance) }

            surface = getProperty { createSurface(it, instance).value }
            // TODO: Reserve to destroy surface

            physicalDevices = getProperties<VkPhysicalDevice> { vk.enumeratePhysicalDevices(instance, it).value }
                    .map { add(VKPhysicalDevice(it, surface)) }
            primaryPhysicalDevice = physicalDevices.first()
            primaryLogicalDevice = add(primaryPhysicalDevice.createDevice(
                    emptyList(), // TODO: Allow arbitrary values ​​to be passed
                    listOf("VK_KHR_swapchain")
            ))

            primaryGraphicCommandPool = add(primaryLogicalDevice.primaryGraphicQueue.createCommandPool())

            swapchainRecreator = add(VKSwapchainRecreator(
                    surface,
                    primaryLogicalDevice,
                    primaryGraphicCommandPool,
                    VkExtent2D(windowSize.x, windowSize.y)
            ))

            imageAvailableSemaphores = List(swapchainRecreator.current.swapchainImages.size) {
                add(primaryLogicalDevice.createSemaphore())
            }
            renderCompleteSemaphores = List(swapchainRecreator.current.swapchainImages.size) {
                add(primaryLogicalDevice.createSemaphore())
            }
            inFlightFences = List(min(maxFrameInFlight, swapchainRecreator.current.swapchainImages.size)) {
                add(primaryLogicalDevice.createFence())
            }

            currentRenderContext = VKRenderContext(
                    swapchainRecreator.current.commandBuffers.first(),
                    swapchainRecreator.current.framebuffers.first(),
                    swapchainRecreator.current.swapchainImages.first(),
                    swapchainRecreator.current.depthResources.first()
            )

            emptyTexture = loadTexture(Image(byteArrayOf(-1, -1, -1, -1), Vector2I(1, 1)))
            add(emptyTexture)
        } catch (e: Error) {
            dispose()
            throw e
        }
    }

    override fun begin() {
        // Do noting
        if (mustRecreateSwapchainInFrame) {
            primaryLogicalDevice.primaryGraphicQueue.queueWaitIdle()
            swapchainRecreator.recreate(VkExtent2D(windowSize.x, windowSize.y))
            mustRecreateSwapchainInFrame = false
        }

        val currentInFlightFence = inFlightFences[currentFrame]
        val currentImageAvailableSemaphore = imageAvailableSemaphores[currentFrame]

        primaryLogicalDevice.waitForFences(listOf(currentInFlightFence))
        primaryLogicalDevice.resetFences(listOf(currentInFlightFence))

        currentSwapchainImageIndex = swapchainRecreator.current.swapchain
                .acquireNextImageKHR(currentImageAvailableSemaphore, null)
        currentSwapchainImageIndexManager.index = currentSwapchainImageIndex

        currentRenderContext = VKRenderContext(
                swapchainRecreator.current.commandBuffers[currentSwapchainImageIndex],
                swapchainRecreator.current.framebuffers[currentSwapchainImageIndex],
                swapchainRecreator.current.swapchainImages[currentSwapchainImageIndex],
                swapchainRecreator.current.depthResources[currentSwapchainImageIndex]
        )

        val usage = listOf(VkCommandBufferUsageFlagBits.VK_COMMAND_BUFFER_USAGE_SIMULTANEOUS_USE_BIT)
        val beginInfo = VkCommandBufferBeginInfo(VkStructureType.VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO, usage, null)
        currentRenderContext.commandBuffer.resetCommandBuffer()
        currentRenderContext.commandBuffer.beginCommandBuffer(beginInfo)
        currentRenderContext.commandBuffer.transitionImageLayout(
                currentRenderContext.swapchainImage,
                VK_IMAGE_LAYOUT_PRESENT_SRC_KHR,
                VK_IMAGE_LAYOUT_GENERAL
        )
    }

    override fun end() {
        currentRenderContext.commandBuffer.transitionImageLayout(
                currentRenderContext.swapchainImage,
                VK_IMAGE_LAYOUT_UNDEFINED,
                VK_IMAGE_LAYOUT_PRESENT_SRC_KHR
        )
        currentRenderContext.commandBuffer.endCommandBuffer()

        val currentInFlightFence = inFlightFences[currentFrame]
        val currentImageAvailableSemaphore = imageAvailableSemaphores[currentFrame]
        val currentRenderCompleteSemaphore = renderCompleteSemaphores[currentFrame]

        val submitInfo = VkSubmitInfo(
                VkStructureType.VK_STRUCTURE_TYPE_SUBMIT_INFO,
                listOf(currentImageAvailableSemaphore.semaphore),
                listOf(VkPipelineStageFlagBits.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT),
                listOf(currentRenderContext.commandBuffer.commandBuffer),
                listOf(currentRenderCompleteSemaphore.semaphore)
        )

        primaryLogicalDevice.primaryGraphicQueue.queueSubmit(listOf(submitInfo), currentInFlightFence)

        val presentInfo = VkPresentInfoKHR(
                VkStructureType.VK_STRUCTURE_TYPE_PRESENT_INFO_KHR,
                listOf(currentRenderCompleteSemaphore.semaphore),
                listOf(swapchainRecreator.current.swapchain.swapchain),
                listOf(currentSwapchainImageIndex),
                null
        )
        val queuePresentResult = primaryLogicalDevice.primaryPresentQueue.queuePresentKHR(presentInfo)
        if (queuePresentResult == VkResult.VK_ERROR_OUT_OF_DATE_KHR || queuePresentResult == VkResult.VK_SUBOPTIMAL_KHR) {
            mustRecreateSwapchainInFrame = true
        }
        currentFrame = (currentFrame + 1) % inFlightFences.size
    }

    override fun resize(windowSize: Vector2I) {
        this.windowSize = windowSize
        mustRecreateSwapchainInFrame = true
    }

    override fun createVertexBuffer(vertices: FloatArray, bufferStorageMode: BufferStorageMode): VertexBuffer {
        return VKVertexBuffer(primaryLogicalDevice, primaryGraphicCommandPool, vertices, bufferStorageMode)
    }

    override fun drawTriangles(batchBufferBundle: BatchBufferBundle) {
        val graphicsPipeline = currentGraphicsPipeline ?: return
        val buffers = batchBufferBundle.let {
            listOf(
                    it.positionBuffer.vertexBuffer as VKVertexBuffer,
                    it.colorBuffer.vertexBuffer as VKVertexBuffer,
                    it.texcoordBuffer.vertexBuffer as VKVertexBuffer
            )
        }

        val descriptorSetUniformProviders = graphicsPipeline.descriptorSetUniformProviders
        val descriptorSetTextureProviders = graphicsPipeline.descriptorSetTextureProviders

        // TODO: Improve performance
        graphicsPipeline.uniforms.forEachIndexed { index, uniform ->
            uniform.bind(descriptorSetUniformProviders[index].value)
        }
        graphicsPipeline.uniformTextures.forEachIndexed { index, texture ->
            texture.bind(descriptorSetTextureProviders[index].value)
        }

        val writeDescriptorSets = mutableListOf<VkWriteDescriptorSet>()
        writeDescriptorSets.addAll(descriptorSetTextureProviders.mapNotNull { it.value.writeDescriptorSet() })
        writeDescriptorSets.addAll(descriptorSetUniformProviders.mapNotNull { it.value.writeDescriptorSet() })
        if (writeDescriptorSets.isNotEmpty()) {
            primaryLogicalDevice.updateDescriptorSets(writeDescriptorSets)
        }

        currentRenderContext.commandBuffer.cmdBeginRenderPass(
                swapchainRecreator.current.renderPass,
                currentRenderContext.framebuffer,
                VkRect2D(VkOffset2D(0, 0), swapchainRecreator.current.extent),
                listOf(
                        VkClearValue(VkClearColorValue(0.5f, 0.5f, 0.5f, 1.0f)),
                        VkClearValue(VkClearDepthStencilValue(1.0f, 0))
                )
        )

        currentRenderContext.commandBuffer.cmdBindDescriptorSets(
                graphicsPipeline.pipelineLayout,
                0,
                listOf(graphicsPipeline.descriptorSet.value)
        )
        currentRenderContext.commandBuffer.cmdBindVertexBuffers(buffers)
        currentRenderContext.commandBuffer.cmdDraw(batchBufferBundle.size, 1, 0, 0)
        currentRenderContext.commandBuffer.cmdEndRenderPass()
    }

    override fun createShader(shaderSource: ShaderSource): Shader {
        return VKShader(primaryLogicalDevice, shaderSource)
    }

    override fun createGraphicsPipeline(shaderProgram: ShaderProgram, config: GraphicsPipelineConfig): GraphicsPipeline {
        val localDisposer = Disposer()
        try {
            val shader = shaderProgram.shader as VKShader
            val uniforms = shaderProgram.descriptorSets.filterIsInstance<VKUniform>()
            val uniformTextures = shaderProgram.descriptorSets.filterIsInstance<VKUniformTexture>()

            val descriptorSetLayout = primaryLogicalDevice.createDescriptorSetLayout(shaderProgram.descriptorSets.map {
                convertToDescriptorSetLayoutBinding(it)
            })
            localDisposer.add(descriptorSetLayout)

            val pipelineLayout = primaryLogicalDevice.createPipelineLayout(descriptorSetLayout)
            localDisposer.add(pipelineLayout)

            val pipeline = primaryLogicalDevice.createPipeline(
                    null,
                    pipelineLayout,
                    swapchainRecreator.current.renderPass,
                    shader.vertShaderModule,
                    shader.fragShaderModule,
                    config.depthTest,
                    config.cullMode.toVkCullModeFlagBits(),
                    config.polygonMode.toVkPolygonMode(),
                    config.srcBlendFactor.toVkBlendFactor(),
                    config.dstBlendFactor.toVkBlendFactor()
            )
            localDisposer.add(pipeline)

            val descriptorPool = primaryLogicalDevice.createDescriptorPool()
            localDisposer.add(descriptorPool)

            val descriptorSets = descriptorPool.allocateDescriptorSets(
                    List(swapchainRecreator.size) { descriptorSetLayout }
            )
            localDisposer.add(descriptorSets)

            val descriptorSet = VKValuePerSwapchainImage(currentSwapchainImageIndexManager, descriptorSets)
            val descriptorSetUniformProviders = uniforms.map { uniform ->
                VKValuePerSwapchainImage(
                        currentSwapchainImageIndexManager,
                        descriptorSets.mapIndexed { index, d ->
                            VKDescriptorSetUniformProvider(d).also {
                                it.set(uniform.binding, uniform.buffer.get(index))
                            }
                        }
                )
            }

            val descriptorSetSamplerProviders = uniformTextures.map {
                VKValuePerSwapchainImage(
                        currentSwapchainImageIndexManager,
                        descriptorSets.map { VKDescriptorSetTextureProvider(it) }
                )
            }

            val graphicsPipeline = VKGraphicsPipeline(
                    shaderProgram,
                    config,
                    pipeline,
                    pipelineLayout,
                    uniforms,
                    uniformTextures,
                    descriptorSetLayout,
                    descriptorPool,
                    descriptorSet,
                    descriptorSetUniformProviders,
                    descriptorSetSamplerProviders
            )
            graphicsPipeline.add(localDisposer)
            return graphicsPipeline
        } catch (e: Error) {
            localDisposer.dispose()
            throw e
        }
    }

    override fun bindGraphicsPipeline(graphicsPipeline: GraphicsPipeline) {
        check(graphicsPipeline is VKGraphicsPipeline)
        currentGraphicsPipeline = graphicsPipeline
        currentRenderContext.commandBuffer.cmdBindPipeline(graphicsPipeline.pipeline)
    }

    override fun setViewport(viewport: RectI) {
        currentRenderContext.commandBuffer.cmdSetViewport(VkViewport(
                viewport.origin.x.toFloat(),
                viewport.size.y.toFloat() - viewport.origin.y.toFloat(),
                viewport.size.x.toFloat(),
                -viewport.size.y.toFloat(),
                0.0f,
                1.0f
        ))
    }

    override fun setScissor(scissor: RectI) {
        currentRenderContext.commandBuffer.cmdSetScissor(VkRect2D(
                VkOffset2D(scissor.origin.x, scissor.origin.y),
                VkExtent2D(scissor.size.x, scissor.size.y)
        ))
    }

    override fun clearColor(color: Vector4F) {
        currentRenderContext.commandBuffer.cmdClearColorImage(
                currentRenderContext.swapchainImage,
                VkClearColorValue(color.x, color.y, color.z, color.w),
                listOf(VkImageSubresourceRange(listOf(VkImageAspectFlagBits.VK_IMAGE_ASPECT_COLOR_BIT), 0, 1, 0, 1)))
    }

    override fun clearDepth(depth: Float) {
        currentRenderContext.commandBuffer.cmdClearDepthStencilImage(
                currentRenderContext.depthResource.depthImage,
                VkClearDepthStencilValue(depth, 0),
                listOf(VkImageSubresourceRange(listOf(VkImageAspectFlagBits.VK_IMAGE_ASPECT_DEPTH_BIT), 0, 1, 0, 1)))
    }

    override fun loadTexture(image: Image, config: Texture.Config): Texture {
        return VKTexture(primaryLogicalDevice, primaryGraphicCommandPool, image, config)
    }

    override fun emptyTexture(): Texture {
        return emptyTexture
    }

    override fun createUniform1I(binding: Int, uniformName: String): Uniform1I {
        val buffers = List(swapchainRecreator.size) {
            VKUniformBuffer(primaryLogicalDevice, Uniform1I.SIZE)
        }
        return VKUniform1I(
                VKValuePerSwapchainImage(currentSwapchainImageIndexManager, buffers),
                binding,
                uniformName
        )
    }

    override fun createUniform1F(binding: Int, uniformName: String): Uniform1F {
        val buffers = List(swapchainRecreator.size) {
            VKUniformBuffer(primaryLogicalDevice, Uniform1F.SIZE)
        }
        val uniform = VKUniform1F(
                VKValuePerSwapchainImage(currentSwapchainImageIndexManager, buffers),
                binding,
                uniformName
        )
        uniform.add(buffers)
        return uniform
    }

    override fun createUniform2F(binding: Int, uniformName: String): Uniform2F {
        val buffers = List(swapchainRecreator.size) {
            VKUniformBuffer(primaryLogicalDevice, Uniform2F.SIZE)
        }
        val uniform = VKUniform2F(
                VKValuePerSwapchainImage(currentSwapchainImageIndexManager, buffers),
                binding,
                uniformName
        )
        uniform.add(buffers)
        return uniform
    }

    override fun createUniform3F(binding: Int, uniformName: String): Uniform3F {
        val buffers = List(swapchainRecreator.size) {
            VKUniformBuffer(primaryLogicalDevice, Uniform3F.SIZE)
        }
        val uniform = VKUniform3F(
                VKValuePerSwapchainImage(currentSwapchainImageIndexManager, buffers),
                binding,
                uniformName
        )
        uniform.add(buffers)
        return uniform
    }

    override fun createUniform4F(binding: Int, uniformName: String): Uniform4F {
        val buffers = List(swapchainRecreator.size) {
            VKUniformBuffer(primaryLogicalDevice, Uniform4F.SIZE)
        }
        val uniform = VKUniform4F(
                VKValuePerSwapchainImage(currentSwapchainImageIndexManager, buffers),
                binding,
                uniformName
        )
        uniform.add(buffers)
        return uniform
    }

    override fun createUniformMatrix4F(binding: Int, uniformName: String): UniformMatrix4F {
        val buffers = List(swapchainRecreator.size) {
            VKUniformBuffer(primaryLogicalDevice, UniformMatrix4F.SIZE)
        }
        val uniform = VKUniformMatrix4F(
                VKValuePerSwapchainImage(currentSwapchainImageIndexManager, buffers),
                binding,
                uniformName
        )
        uniform.add(buffers)
        return uniform
    }

    override fun createUniformTexture(binding: Int, uniformName: String): UniformTexture {
        return VKUniformTexture(binding, uniformName)
    }

    override fun dispose() {
        primaryLogicalDevice.deviceWaitIdle()
        super.dispose()
    }
}
