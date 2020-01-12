package io.github.inoutch.kotchan.core.io.file

import com.autodesk.coroutineworker.CoroutineWorker
import io.github.inoutch.kotchan.utility.Path
import kotlinx.cinterop.BooleanVar
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSDataWritingAtomic
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.dataWithContentsOfFile
import platform.Foundation.writeToFile

@ExperimentalUnsignedTypes
actual class File actual constructor() {

    actual fun readBytes(filepath: String): ByteArray? {
        val fileData = NSData.dataWithContentsOfFile(filepath) ?: return null
        return fileData.bytes?.readBytes(fileData.length.toInt())
    }

    actual fun readBytesAsync(filepath: String): Deferred<ByteArray?> = GlobalScope.async(Dispatchers.Unconfined) {
        CoroutineWorker.withContext(Dispatchers.Unconfined) { readBytes(filepath) }
    }

    actual fun writeBytes(writableFilepath: String, bytes: ByteArray): Boolean {
        val data = bytes.usePinned { p ->
            val pointer = p.addressOf(0)
            NSData.create(bytes = pointer, length = bytes.size.toULong())
        }
        val fullPath = getWritablePath(writableFilepath)
        return memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>()
            val ret = data.writeToFile(fullPath, NSDataWritingAtomic, error.ptr)
//            val errorValue = error.value
//            if (errorValue != null) {
//                logger.error("${errorValue.localizedDescription} [code=${errorValue.code}, path=$fullpath]")
//            }
            ret
        }
    }

    actual fun writeBytesAsync(writableFilepath: String, bytes: ByteArray): Deferred<Boolean> = GlobalScope.async(Dispatchers.Unconfined) {
        CoroutineWorker.withContext(Dispatchers.Unconfined) { writeBytes(writableFilepath, bytes) }
    }

    actual fun getResourcePath(name: String): String? {
        val path = if (name.endsWith('/')) name.substring(0, name.length - 1) else name
        return NSBundle.mainBundle.pathForResource(path, ofType = null)
    }

    actual fun getWritablePath(name: String): String {
        val paths = NSSearchPathForDirectoriesInDomains(NSApplicationSupportDirectory, NSUserDomainMask, true)
        return Path.resolve(paths.first() as String, name)
    }

    actual fun getFileList(filepath: String): List<FileItem> {
        val manager = NSFileManager.defaultManager()
        return memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>()
            val files = manager.contentsOfDirectoryAtPath(filepath, error.ptr)
            val errorValue = error.value
            if (errorValue != null) {
//                logger.error("${errorValue.localizedDescription} [code=${errorValue.code}, path=$filepath]")
                return@memScoped emptyList()
            } else if (files == null) {
                return@memScoped emptyList()
            }
            files.map {
                val name = it as String
                val isDirectory = alloc<BooleanVar>()
                manager.fileExistsAtPath(Path.resolve(filepath, name), isDirectory.ptr)
                FileItem(name, if (isDirectory.value) FileType.Directory else FileType.File)
            }
        }
    }

    actual fun getFileListAsync(filepath: String): Deferred<List<FileItem>> = GlobalScope.async(Dispatchers.Unconfined) {
        CoroutineWorker.withContext(Dispatchers.Unconfined) { getFileList(filepath) }
    }

    actual fun makeDirectory(writablePath: String): Boolean {
        return NSFileManager.defaultManager().createDirectoryAtPath(getWritablePath(writablePath), true, null, null)
    }

    actual fun makeDirectories(writablePath: String): Boolean {
        return NSFileManager.defaultManager().createDirectoryAtPath(getWritablePath(writablePath), true, null, null)
    }
}
