package io.github.inoutch.kotchan.core.graphic.compatible

import io.github.inoutch.kotchan.math.Vector2I
import io.github.inoutch.kotlin.vulkan.extension.toByteArray
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGColorSpaceRelease
import platform.CoreGraphics.CGContextDrawImage
import platform.CoreGraphics.CGContextRelease
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGImageGetColorSpace
import platform.CoreGraphics.CGImageGetHeight
import platform.CoreGraphics.CGImageGetWidth
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.kCGBitmapByteOrder32Big
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes

actual class Image private actual constructor(
        actual val byteArray: ByteArray,
        actual val size: Vector2I
) {
    actual companion object {
        @ExperimentalUnsignedTypes
        actual fun loadPNGByteArrayAsync(byteArray: ByteArray): Deferred<Image> = loadByteArray(byteArray)

        @ExperimentalUnsignedTypes
        fun loadByteArray(byteArray: ByteArray): Deferred<Image> = GlobalScope.async(Dispatchers.Unconfined) {
            byteArray.usePinned {
                val data = NSData.dataWithBytes(it.addressOf(0), byteArray.size.toULong())
                val image = platform.UIKit.UIImage.imageWithData(data)
                checkNotNull(image) { "Invalid image format" }

                val imageRef = image.CGImage()
                val width = CGImageGetWidth(imageRef)
                val height = CGImageGetHeight(imageRef)
                val colorSpace = CGImageGetColorSpace(imageRef)
                val size = Vector2I(width.toInt(), height.toInt())

                val byteSize = size.x * size.y * 4
                val retByteArray = memScoped {
                    val raw = allocArray<ByteVar>(byteSize)
                    val context = CGBitmapContextCreate(
                            raw, width, height,
                            8,
                            4u * width, colorSpace,
                            CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value or
                                    kCGBitmapByteOrder32Big)
                    val rect = CGRectMake(0.0, 0.0, width.toInt().toDouble(), height.toInt().toDouble())
                    CGContextDrawImage(context, rect, imageRef)
                    CGContextRelease(context)
                    raw.toByteArray(byteSize)
                }
                CGColorSpaceRelease(colorSpace)
                Image(retByteArray, size)
            }
        }
    }
}