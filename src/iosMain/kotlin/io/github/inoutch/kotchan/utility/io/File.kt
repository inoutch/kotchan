package io.github.inoutch.kotchan.utility.io

import io.github.inoutch.kotchan.core.KotchanCore.Companion.logger
import kotlinx.cinterop.*
import platform.Foundation.*
import io.github.inoutch.kotchan.extension.toUTF8String
import io.github.inoutch.kotchan.utility.path.Path
import platform.Foundation.NSDataWritingAtomic
import platform.Foundation.create
import platform.Foundation.dataWithContentsOfFile
import platform.posix.err

@ExperimentalUnsignedTypes
actual class File {
    init {
        makeDirectories("")
    }

    actual fun readBytes(filepath: String): ByteArray? {
        val fileData = NSData.dataWithContentsOfFile(filepath) ?: return null
        return fileData.bytes?.readBytes(fileData.length.toInt())
    }

    actual fun readText(filepath: String): String? {
        return readBytes(filepath)?.toUTF8String()
    }

    actual fun writeBytes(writableFilepath: String, bytes: ByteArray): Boolean {
        val data = bytes.usePinned { p ->
            val pointer = p.addressOf(0)
            NSData.create(bytes = pointer, length = bytes.size.toULong())
        }
        val fullpath = getWritablePath(writableFilepath)

        return memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>()
            val ret = data.writeToFile(fullpath, NSDataWritingAtomic, error.ptr)
            val errorValue = error.value
            if (errorValue != null) {
                logger.error("${errorValue.localizedDescription} [code=${errorValue.code}, path=$fullpath]")
            }
            ret
        }
    }

    actual fun writeText(writableFilepath: String, text: String): Boolean {
        return writeBytes(writableFilepath, text.encodeToByteArray())
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
        return NSFileManager.defaultManager().createDirectoryAtPath(getWritablePath(writablePath), true, null, null)
    }

    actual fun makeDirectories(writablePath: String): Boolean {
        return NSFileManager.defaultManager().createDirectoryAtPath(getWritablePath(writablePath), true, null, null)
    }
}
