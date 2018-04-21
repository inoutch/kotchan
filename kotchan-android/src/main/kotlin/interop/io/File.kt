package interop.io

import com.example.app.MainActivity
import interop.extension.toUTF8String
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
        return readBytes(filepath)?.toUTF8String()
    }

    actual fun getResourcePath(name: String): String? {

        return name
    }
}