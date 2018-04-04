package interop.io

expect class File() {
    fun readBytes(filepath: String): ByteArray?
    fun readText(filepath: String): String?
    fun getResourcePath(name: String): String?
}