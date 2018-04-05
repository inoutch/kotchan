package interop.io

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

actual class File {
    actual fun readBytes(filepath: String): ByteArray? {
        try {
            FileInputStream(File(filepath)).use {
                return it.readBytes()
            }
        } catch (e: FileNotFoundException) {
            return null
        }
    }

    actual fun readText(filepath: String): String? {
        return readBytes(filepath)?.let { String() }
    }

    actual fun getResourcePath(name: String): String? {
        return ClassLoader.getSystemResource(name)?.path
    }
}