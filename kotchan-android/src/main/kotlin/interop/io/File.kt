package interop.io

import kotchan.MainActivity
import interop.extension.toUTF8String
import kotchan.logger.logger
import java.io.File
import java.io.FileNotFoundException

actual class File {
    actual fun readBytes(filepath: String): ByteArray? {
        try {
            MainActivity.getInputStream(filepath)?.use {
                return it.readBytes()
            }
        } catch (e: Exception) {
            logger.error(e)
            return null
        }
        return null
    }

    actual fun readText(filepath: String): String? {
        return readBytes(filepath)?.toUTF8String()
    }

    actual fun writeBytes(writableFilepath: String, bytes: ByteArray): Boolean {
        return try {
            MainActivity.getOutputStream(getWritablePath(writableFilepath)).use {
                it.write(bytes)
                true
            }
        } catch (e: FileNotFoundException) {
            false
        }
    }

    actual fun writeText(writableFilepath: String, text: String): Boolean {
        return writeBytes(writableFilepath, text.toByteArray(Charsets.UTF_8))
    }

    actual fun getResourcePath(name: String): String? {
        return "@assets:$name"
    }

    actual fun getWritablePath(name: String): String {
        return MainActivity.getFilesDir(name)
    }

    actual fun makeDirectory(writablePath: String): Boolean {
        return File(getWritablePath(writablePath)).mkdir()
    }

    actual fun makeDirectories(writablePath: String): Boolean {
        return File(getWritablePath(writablePath)).mkdirs()
    }
}