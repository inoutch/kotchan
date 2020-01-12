package io.github.inoutch.kotchan.core.io.file

import io.github.inoutch.kotchan.extension.toUTF8String
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

expect class File() {

    fun readBytes(filepath: String): ByteArray?

    fun readBytesAsync(filepath: String): Deferred<ByteArray?>

    fun writeBytes(writableFilepath: String, bytes: ByteArray): Boolean

    fun writeBytesAsync(writableFilepath: String, bytes: ByteArray): Deferred<Boolean>

    fun getResourcePath(name: String): String?

    fun getWritablePath(name: String): String

    fun getFileList(filepath: String): List<FileItem>

    fun getFileListAsync(filepath: String): Deferred<List<FileItem>>

    fun makeDirectory(writablePath: String): Boolean

    fun makeDirectories(writablePath: String): Boolean
}

@kotlin.ExperimentalStdlibApi
fun File.readText(filepath: String) = readBytes(filepath)?.toUTF8String()

@kotlin.ExperimentalStdlibApi
fun File.readTextAsync(filepath: String): Deferred<String?> = GlobalScope.async {
    val result = readBytesAsync(filepath).await()
    result?.toUTF8String()
}

fun File.readBytesFromResource(filepath: String): ByteArray? =
        getResourcePath(filepath)?.let { readBytes(it) }

@kotlin.ExperimentalStdlibApi
fun File.readBytesFromResourceAsync(filepath: String): Deferred<ByteArray?> = GlobalScope.async {
    getResourcePath(filepath)?.let { readBytesAsync(filepath).await() }
}

@kotlin.ExperimentalStdlibApi
fun File.readTextFromResource(filepath: String) =
        getResourcePath(filepath)?.let { readText(it) }

@kotlin.ExperimentalStdlibApi
fun File.readTextFromResourceAsync(filepath: String): Deferred<String?> = GlobalScope.async {
    getResourcePath(filepath)?.let { readTextAsync(filepath).await() }
}

fun File.readBytesFromResourceWithError(filepath: String): ByteArray =
        readBytesFromResource(filepath) ?: throw NoSuchFileError(filepath)

@kotlin.ExperimentalStdlibApi
fun File.readBytesFromResourceWithErrorAsync(filepath: String): Deferred<ByteArray> = GlobalScope.async {
    readBytesFromResourceAsync(filepath).await() ?: throw NoSuchFileError(filepath)
}

@kotlin.ExperimentalStdlibApi
fun File.readTextFromResourceWithError(filepath: String): String =
        readTextFromResource(filepath) ?: throw NoSuchFileError(filepath)

@kotlin.ExperimentalStdlibApi
fun File.readTextFromResourceWithErrorAsync(filepath: String): Deferred<String> = GlobalScope.async {
    readTextFromResourceAsync(filepath).await() ?: throw NoSuchFileError(filepath)
}

fun File.getResourcePathWithError(name: String) =
        getResourcePath(name) ?: throw NoSuchDirectoryError(name)
