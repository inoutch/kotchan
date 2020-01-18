package io.github.inoutch.kotchan.core.platform

import eaglview.EAGLView
import io.github.inoutch.kotchan.math.Vector2I
import platform.EAGL.EAGLContext
import platform.EAGL.presentRenderbuffer
import platform.gles2.GL_FRAMEBUFFER
import platform.gles2.GL_RENDERBUFFER
import platform.gles2.glBindFramebuffer
import platform.gles2.glBindRenderbuffer
import platform.gles2.glViewport
import platform.glescommon.GLuint

class KotchanEAGLContext(
        val context: EAGLContext,
        val view: EAGLView,
        val framebuffer: GLuint,
        val colorRenderbuffer: GLuint,
        val framebufferSize: Vector2I
) {
    fun setContext() {
        EAGLContext.setCurrentContext(context)
    }

    fun setFramebuffer() {
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer)
        glViewport(0, 0, framebufferSize.x, framebufferSize.y)
    }

    fun presentFramebuffer() {
        glBindRenderbuffer(GL_RENDERBUFFER, colorRenderbuffer)
        val isSucceeded = context.presentRenderbuffer(GL_RENDERBUFFER)
        check(isSucceeded) { "Failed to present renderbuffer" }
    }
}
