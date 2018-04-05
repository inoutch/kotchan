package kotchan.scene.drawable

import kotchan.texture.TextureAtlas
import utility.type.Size

class Sprite(private val textureAtlas: TextureAtlas)
    : Square(Size(textureAtlas.texture.width.toFloat(),
        textureAtlas.texture.height.toFloat())) {

}