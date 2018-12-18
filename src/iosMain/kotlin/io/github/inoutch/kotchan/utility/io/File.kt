package io.github.inoutch.kotchan.utility.io

import kotlinx.cinterop.*
import platform.Foundation.*
import io.github.inoutch.kotchan.extension.toUTF8String
import io.github.inoutch.kotchan.utility.path.Path
import platform.Foundation.NSDataWritingAtomic
import platform.Foundation.create
import platform.Foundation.dataWithContentsOfFile

actual class File {

    actual fun readBytes(filepath: String): ByteArray? {
        val fileData = NSData.dataWithContentsOfFile(filepath) ?: return null
        return fileData.bytes?.readBytes(fileData.length.toInt())
    }

    actual fun readText(filepath: String): String? {
        return readBytes(filepath)?.toUTF8String()
    }

    actual fun writeBytes(writableFilepath: String, bytes: ByteArray): Boolean {
        return bytes.usePinned { p ->
            val pointer = p.addressOf(0)
            NSData.create(bytes = pointer, length = bytes.size.toULong())
        }.writeToFile(getWritablePath(writableFilepath), NSDataWritingAtomic, null)
    }

    actual fun writeText(writableFilepath: String, text: String): Boolean {
        return writeBytes(writableFilepath, text.toUtf8())
    }

    actual fun getResourcePath(name: String): String? {
        val path = if (name.endsWith('/')) name.substring(0, name.length - 1) else name
        return NSBundle.mainBundle.pathForResource(path, ofType = null)
    }

    actual fun getWritablePath(name: String): String {
        val paths = NSSearchPathForDirectoriesInDomains(NSApplicationSupportDirectory, NSUserDomainMask, true)
        return Path.resolve(paths.first() as String, name)
    }

    actual fun makeDirectory(writablePath: String): Boolean {
        return NSFileManager.defaultManager().createDirectoryAtPath(getWritablePath(writablePath), false, null, null)
    }

    actual fun makeDirectories(writablePath: String): Boolean {
        return NSFileManager.defaultManager().createDirectoryAtPath(getWritablePath(writablePath), true, null, null)
    }
}