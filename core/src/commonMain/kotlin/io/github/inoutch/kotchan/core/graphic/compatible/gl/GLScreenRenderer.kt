package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.config
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.renderer.ScreenRenderer
import io.github.inoutch.kotlin.gl.api.GL_COLOR_ATTACHMENT0
import io.github.inoutch.kotlin.gl.api.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE
import io.github.inoutch.kotlin.gl.api.GL_RENDERBUFFER
import io.github.inoutch.kotlin.gl.api.GL_RGB
import io.github.inoutch.kotlin.gl.api.GL_TEXTURE_2D
import io.github.inoutch.kotlin.gl.api.GL_VIEWPORT
import io.github.inoutch.kotlin.gl.api.gl

class GLScreenRenderer : ScreenRenderer() {
    override fun begin() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun end() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createTexture(): Texture {
        val framebuffer = gl.getFramebufferAttachmentParameteriv(
                GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE,
                GL_COLOR_ATTACHMENT0,
                GL_RENDERBUFFER
        )
        val framebufferWidth = gl.getIntegerv(GL_VIEWPORT)
        val texture = gl.genTextures(1)
        gl.copyTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, 0, 0, config.viewportSize.x, config.viewportSize.y, 0)
        TODO()
    }
}