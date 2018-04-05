package kotchan.texture

import interop.graphic.GLTexture
import kotchan.tool.TextureFrame

data class TextureAtlas(val frames: List<TextureFrame>, val texture: GLTexture)