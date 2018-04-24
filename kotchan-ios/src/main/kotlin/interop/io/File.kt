package interop.io

import kotlinx.cinterop.*
import platform.Foundation.*
import interop.extension.*

actual class File {
    actual fun readBytes(filepath: String): ByteArray? {
        val fileData = NSData.dataWithContentsOfFile(filepath) ?: return null
        return fileData.bytes?.readBytes(fileData.length.toInt())
    }

    actual fun readText(filepath: String): String? {
        return readBytes(filepath)?.toUTF8String()
    }

    actual fun getResourcePath(name: String): String? {
        val path = if (name.endsWith('/')) name.substring(0, name.length - 1) else name
        return NSBundle.mainBundle.pathForResource(path, ofType = null)?.toString()
    }
}