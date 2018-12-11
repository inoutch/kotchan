package interop.io

expect class File() {

    fun readBytes(filepath: String): ByteArray?

    fun readText(filepath: String): String?

    fun writeBytes(writableFilepath: String, bytes: ByteArray): Boolean

    fun writeText(writableFilepath: String, text: String): Boolean

    fun getResourcePath(name: String): String?

    fun getWritablePath(name: String): String

    fun makeDirectory(writablePath: String): Boolean

    fun makeDirectories(writablePath: String): Boolean
}