package kotchan.view.drawable

import interop.graphic.GLShaderProgram
import kotchan.view.Texture
import utility.type.Mesh

abstract class Drawable(
        val shaderProgram: GLShaderProgram,
        val mesh: Mesh,
        val texture: Texture = Texture.empty)