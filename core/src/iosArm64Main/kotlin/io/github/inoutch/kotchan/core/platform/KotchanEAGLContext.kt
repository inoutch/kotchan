package io.github.inoutch.kotchan.core.platform

import eaglview.EAGLView
import io.github.inoutch.kotchan.math.Vector2I
import platform.EAGL.EAGLContext
import platform.glescommon.GLuint

data class KotchanEAGLContext(
        val context: EAGLContext,
        val view: EAGLView,
        val framebuffer: GLuint,
        val colorRenderbuffer: GLuint,
        val framebufferSize: Vector2I
)
