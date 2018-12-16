package io.github.inoutch.kotchan.utility.io

import io.github.inoutch.kotchan.android.KotchanActivity
import io.github.inoutch.kotchan.core.logger.logger
import io.github.inoutch.kotchan.extension.toUTF8String
import java.io.File
import java.io.FileNotFoundException

actual class File {
    actual fun readBytes(filepath: String) = try {
        KotchanActivity.inputStream(filepath)?.use { it.readBytes() }
    } catch (e: Exception) {
        logger.error(e)
        null
    }

    actual fun readText(filepath: String): String? {
        return readBytes(filepath)?.toUTF8String()
    }

    actual fun writeBytes(writableFilepath: String, bytes: ByteArray) = try {
        KotchanActivity.outputStream(getWritablePath(writableFilepath)).use {
            it.write(bytes)
            true
        }
    } catch (e: FileNotFoundException) {
        false
    }

    actual fun writeText(writableFilepath: String, text: String): Boolean {
        return writeBytes(writableFilepath, text.toByteArray(Charsets.UTF_8))
    }

    actual fun getResourcePath(name: String): String? {
        return "@assets:$name"
    }

    actual fun getWritablePath(name: String): String {
        return KotchanActivity.filesDir(name)
    }

    actual fun makeDirectory(writablePath: String): Boolean {
        return File(getWritablePath(writablePath)).mkdir()
    }

    actual fun makeDirectories(writablePath: String): Boolean {
        return File(getWritablePath(writablePath)).mkdirs()
    }
}