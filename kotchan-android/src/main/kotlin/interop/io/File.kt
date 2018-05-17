package interop.io

import kotchan.MainActivity
import interop.extension.toUTF8String
import kotchan.logger.logger

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

    actual fun getResourcePath(name: String): String? {
        return "@assets:$name"
    }
}