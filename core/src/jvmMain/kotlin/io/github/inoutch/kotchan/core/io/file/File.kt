package io.github.inoutch.kotchan.core.io.file

import io.github.inoutch.kotchan.utility.Path
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.net.URLDecoder
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

actual class File actual constructor() {
    actual fun readBytes(filepath: String): ByteArray? {
        val jarScheme = filepath.split("!")
        if (jarScheme.size == 2) {
            return javaClass.getResourceAsStream(jarScheme[1])?.use {
                it.readBytes()
            }
        }

        try {
            FileInputStream(File(filepath)).use {
                return it.readBytes()
            }
        } catch (e: FileNotFoundException) {
            return null
        }
    }

    actual fun readBytesAsync(filepath: String): Deferred<ByteArray?> = GlobalScope.async(Dispatchers.IO) {
        readBytes(filepath)
    }

    actual fun writeBytes(writableFilepath: String, bytes: ByteArray): Boolean {
        return try {
            FileOutputStream(File(getWritablePath(writableFilepath))).use {
                it.write(bytes)
                true
            }
        } catch (e: FileNotFoundException) {
            false
        }
    }

    actual fun writeBytesAsync(writableFilepath: String, bytes: ByteArray): Deferred<Boolean> = GlobalScope.async(Dispatchers.IO) {
        writeBytes(writableFilepath, bytes)
    }

    actual fun getResourcePath(name: String): String? {
        return Thread.currentThread().contextClassLoader.getResource(name)?.path
    }

    actual fun getWritablePath(name: String): String {
        val path = File::class.java.protectionDomain.codeSource.location.path
        val decodedPath = URLDecoder.decode(path, "UTF-8")
        return Path.resolve(File(decodedPath).parent, "data", name)
    }

    actual fun getFileList(filepath: String): List<FileItem> {
        return File(filepath).listFiles()?.map {
            FileItem(it.name, if (it.isFile) FileType.File else FileType.Directory)
        } ?: emptyList()
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
