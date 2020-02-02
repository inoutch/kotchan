package io.github.inoutch.kotchan.core.io.file

import io.github.inoutch.kotchan.core.platform.KotchanFilePlatform
import java.io.File
import java.io.IOException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

actual class File actual constructor() {
    private var filePlatformRaw: KotchanFilePlatform? = null

    private val filePlatform: KotchanFilePlatform
        get() = checkNotNull(filePlatformRaw) { "KotchanFilePlatform is not initialized" }

    fun initialize(filePlatform: KotchanFilePlatform) {
        this.filePlatformRaw = filePlatform
    }

    fun clear() {
        this.filePlatformRaw = null
    }

    actual fun readBytes(filepath: String): ByteArray? = filePlatform.inputStream(filepath)?.use { it.readBytes() }

    actual fun readBytesAsync(filepath: String): Deferred<ByteArray?> = GlobalScope.async(Dispatchers.IO) {
        readBytes(filepath)
    }

    actual fun writeBytes(writableFilepath: String, bytes: ByteArray): Boolean {
        return try {
            filePlatform.outputStream(getWritablePath(writableFilepath)).use { it.write(bytes) }
            true
        } catch (e: IOException) {
            false
        }
    }

    actual fun writeBytesAsync(writableFilepath: String, bytes: ByteArray):
            Deferred<Boolean> = GlobalScope.async(Dispatchers.IO) {
        writeBytes(writableFilepath, bytes)
    }

    actual fun getResourcePath(name: String): String? {
        return "@assets:$name"
    }

    actual fun getWritablePath(name: String): String {
        return filePlatform.filesDir(name)
    }

    actual fun getFileList(filepath: String): List<FileItem> {
        return filePlatform.fileList(filepath)
    }

    actual fun getFileListAsync(filepath: String): Deferred<List<FileItem>> = GlobalScope.async(Dispatchers.IO) {
        getFileList(filepath)
    }

    actual fun makeDirectory(writablePath: String): Boolean {
        return File(getWritablePath(writablePath)).mkdir()
    }

    actual fun makeDirectories(writablePath: String): Boolean {
        return File(getWritablePath(writablePath)).mkdirs()
    }
}
