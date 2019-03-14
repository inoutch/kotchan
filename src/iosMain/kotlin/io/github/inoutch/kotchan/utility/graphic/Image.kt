package io.github.inoutch.kotchan.utility.graphic

import io.github.inoutch.kotchan.extension.toByteArray
import io.github.inoutch.kotchan.utility.type.Point
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.Foundation.NSData
import platform.Foundation.dataWithBytesNoCopy

@ExperimentalUnsignedTypes
actual class Image private actual constructor(actual val byteArray: ByteArray, actual val size: Point) {
    actual companion object {
        actual fun load(byteArray: ByteArray): Image {
            val ret = byteArray.usePinned {
                val data = NSData.dataWithBytesNoCopy(it.addressOf(0), byteArray.size.toULong())
                val image = platform.UIKit.UIImage.imageWithData(data)
                        ?: throw Error("invalid format")

                val imageRef = image.CGImage
                val width = CGImageGetWidth(imageRef)
                val height = CGImageGetHeight(imageRef)
                val colorSpace = CGImageGetColorSpace(imageRef)
                val size = Point(width.toInt(), height.toInt())

                val byteSize = size.x * size.y * 4
                val retByteArray = memScoped {
                    val raw = allocArray<ByteVar>(byteSize)
                    val context = CGBitmapContextCreate(
                            raw,
                            width,
                            height,
                            8,
                            4u * width, colorSpace,
                            CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value and
                                    kCGBitmapByteOrder32Big)
                    CGContextDrawImage(context, CGRectMake(
                            0.0, 0.0, width.toInt().toDouble(), height.toInt().toDouble()), imageRef)
                    CGContextRelease(context)
                    raw.toByteArray(byteSize)
                }
                CGColorSpaceRelease(colorSpace)
                Result(size, retByteArray)
            }
            return Image(ret.byteArray, ret.size)
        }
    }

    private class Result(val size: Point, val byteArray: ByteArray)
}
