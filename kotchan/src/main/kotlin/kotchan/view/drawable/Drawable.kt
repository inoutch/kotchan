package kotchan.view.drawable

import interop.graphic.GLShaderProgram
import kotchan.view.BatchData
import kotchan.view.Texture
import utility.type.Mesh

abstract class Drawable(val shaderProgram: GLShaderProgram) {
    var mesh: Mesh? = null
    var texture: Texture = Texture()
}