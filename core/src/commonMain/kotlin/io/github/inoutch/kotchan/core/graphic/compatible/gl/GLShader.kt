package io.github.inoutch.kotchan.core.graphic.compatible.gl

import io.github.inoutch.kotchan.core.Disposer
import io.github.inoutch.kotchan.core.graphic.compatible.shader.Shader
import io.github.inoutch.kotlin.gl.api.GL_FRAGMENT_SHADER
import io.github.inoutch.kotlin.gl.api.GL_VERTEX_SHADER
import io.github.inoutch.kotlin.gl.api.GLenum
import io.github.inoutch.kotlin.gl.api.GLuint
import io.github.inoutch.kotlin.gl.api.gl

class GLShader(vertSource: String, fragSource: String) : Shader() {
    private val disposer = Disposer()

    val program: GLuint

    init {
        try {
            val vertShader = loadShader(GL_VERTEX_SHADER, vertSource)
            disposer.add(vertShader) { gl.deleteShader(vertShader) }

            val fragShader = loadShader(GL_FRAGMENT_SHADER, fragSource)
            disposer.add(fragShader) { gl.deleteShader(fragShader) }

            program = gl.createProgram()
            gl.attachShader(program, vertShader)
            gl.attachShader(program, fragShader)

            bindAttribute(GLAttribLocation.ATTRIBUTE_POSITION)
            bindAttribute(GLAttribLocation.ATTRIBUTE_COLOR)
            bindAttribute(GLAttribLocation.ATTRIBUTE_TEXCOORD)
            linkShader(program)

            disposer.dispose(vertShader)
            disposer.dispose(fragShader)
        } catch (e: Error) {
            disposer.dispose()
            throw e
        }
    }

    fun use() {
        gl.useProgram(program)
    }

    override fun isDisposed(): Boolean {
        return disposer.isDisposed()
    }

    override fun dispose() {
        disposer.dispose()
    }

    private fun loadShader(type: GLenum, source: String): GLuint {
        val shader = gl.createShader(type)
        check(shader != 0) { "Unable to create shader" }
        try {
            gl.shaderSource(shader, source)
            gl.compileShader(shader)
            val shaderInfoLog = gl.getShaderInfoLog(shader)
            if (shaderInfoLog != null) {
                throw IllegalStateException(shaderInfoLog)
            }
        } catch (e: IllegalStateException) {
            gl.deleteShader(shader)
            throw e
        }
        return shader
    }

    private fun linkShader(program: GLuint) {
        gl.linkProgram(program)
        val programInfoLog = gl.getProgramInfoLog(program)
        if (programInfoLog != null) {
            throw IllegalStateException(programInfoLog)
        }
    }

    private fun bindAttribute(location: GLAttribLocation) {
        gl.bindAttribLocation(program, location.value, location.locationName)
    }
}
