package interop.io

import kotlinx.cinterop.*
import platform.Foundation.*

actual class File {
    actual fun readBytes(filepath: String): ByteArray? {
        val fileData = NSData.dataWithContentsOfFile(filepath) ?: return null
        return fileData.bytes?.readBytes(fileData.length.toInt())
    }

    actual fun readText(filepath: String): String? {
        return readBytes(filepath)?.let { String() }
    }

    actual fun getResourcePath(name: String): String? {
        return NSBundle.mainBundle.pathForResource(name, ofType = null)?.toString()
    }
}