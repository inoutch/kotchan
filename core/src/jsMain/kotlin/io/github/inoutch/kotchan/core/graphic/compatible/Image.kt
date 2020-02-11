package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.extension.toByteArray
import io.github.inoutch.kotchan.math.Vector2I
import kotlin.browser.document
import kotlin.js.Promise
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.asDeferred
import org.khronos.webgl.Uint8Array
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

actual fun loadPNGByteArrayAsync(byteArray: ByteArray): Deferred<Image> {
    val data = Uint8Array(byteArray.toTypedArray())
    val oUrl = URL.createObjectURL(Blob(arrayOf(data), BlobPropertyBag("image/png")))
    val image = org.w3c.dom.Image()
    return Promise<Image> { resolve, reject ->
        image.onload = {
            resolve(convertImage(image))
            URL.revokeObjectURL(oUrl)
        }
        image.onerror = { errMsg, _, _, _, _ ->
            reject(IllegalStateException(errMsg as String))
        }
        image.src = oUrl
    }.asDeferred()
}

private fun convertImage(image: org.w3c.dom.Image): Image {
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.drawImage(image, 0.0, 0.0, image.width.toDouble(), image.height.toDouble())

    val imageData = context.getImageData(0.0, 0.0, image.width.toDouble(), image.height.toDouble())
    return Image(imageData.data.toByteArray(), Vector2I(image.width, image.height))
}
