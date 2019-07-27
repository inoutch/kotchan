package io.github.inoutch.kotchan.utility.io

import io.github.inoutch.kotchan.core.KotchanCore.Companion.logger
import io.github.inoutch.kotchan.extension.toUTF8String
import io.github.inoutch.kotchan.utility.path.Path
import java.io.*
import java.net.URLDecoder

actual class File {
    init {
        makeDirectories("")
    }

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

    actual fun readText(filepath: String): String? {
        return readBytes(filepath)?.toUTF8String()
    }

    actual fun writeBytes(writableFilepath: String, bytes: ByteArray): Boolean {
        return try {
            FileOutputStream(File(getWritablePath(writableFilepath))).use {
                it.write(bytes)
                true
            }
        } catch (e: FileNotFoundException) {
            logger.debug(e.message ?: "FileNotFoundException(null)")
            false
        }
    }

    actual fun writeText(writableFilepath: String, text: String): Boolean {
        return writeBytes(writableFilepath, text.toByteArray(Charsets.UTF_8))
    }

    actual fun getResourcePath(name: String): String? {
        return Thread.currentThread().contextClassLoader.getResource(name)?.path
    }

    actual fun getWritablePath(name: String): String {
        val path = File::class.java.protectionDomain.codeSource.location.path
        val decodedPath = URLDecoder.decode(path, "UTF-8")
        return Path.resolve(File(decodedPath).parent, "data", name)
    }

    actual fun makeDirectory(writablePath: String): Boolean {
        return File(getWritablePath(writablePath)).mkdir()
    }

    actual fun makeDirectories(writablePath: String): Boolean {
        return File(getWritablePath(writablePath)).mkdirs()
    }
}
