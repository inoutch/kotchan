package io.github.inoutch.kotchan.core.io.file

import io.github.inoutch.kotchan.extension.toByteArray
import io.github.inoutch.kotchan.utility.Path
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.asDeferred
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.w3c.xhr.ARRAYBUFFER
import org.w3c.xhr.XMLHttpRequest
import org.w3c.xhr.XMLHttpRequestResponseType
import kotlin.browser.localStorage
import kotlin.js.Promise

actual class File actual constructor() {
    private val json = Json(JsonConfiguration.Stable)

    @ExperimentalStdlibApi
    actual fun readBytes(filepath: String): ByteArray? {
        throw UnsupportedOperationException()
    }

    @ExperimentalStdlibApi
    actual fun readBytesAsync(filepath: String): Deferred<ByteArray?> {
        return Promise<ByteArray?> { resolve, _ ->
            if (filepath.startsWith("@ls://")) {
                val contents = localStorage.getItem(filepath)
                resolve(contents?.encodeToByteArray())
                return@Promise
            }
            val req = XMLHttpRequest()
            req.open("GET", filepath, true)
            req.responseType = XMLHttpRequestResponseType.Companion.ARRAYBUFFER
            req.onload = onload@{
                if (req.readyState != 4.toShort()) {
                    return@onload Unit
                }
                if (req.status == 200.toShort()) {
                    val bytes = Uint8Array(req.response as ArrayBuffer)
                    resolve(bytes.toByteArray())
                } else {
                    resolve(null)
                }
            }
            req.onerror = {
                resolve(null)
            }
            req.send(null)
        }.asDeferred()
    }

    @ExperimentalStdlibApi
    actual fun writeBytes(writableFilepath: String, bytes: ByteArray): Boolean {
        check(writableFilepath.startsWith("@ls://")) {
            "This file path cannot be written [$writableFilepath]"
        }
        return try {
            localStorage.setItem(writableFilepath, bytes.decodeToString())
            true
        } catch (e: Error) {
            false
        }
    }

    @ExperimentalStdlibApi
    actual fun writeBytesAsync(writableFilepath: String, bytes: ByteArray): Deferred<Boolean> {
        return Promise<Boolean> { resolve, _ ->
            resolve(writeBytes(writableFilepath, bytes))
        }.asDeferred()
    }

    actual fun getResourcePath(name: String): String? {
        return Path.resolve("/", name)
    }

    actual fun getWritablePath(name: String): String {
        return Path.resolve("@ls://", name)
    }

    actual fun getFileList(filepath: String): List<FileItem> {
        val string = localStorage.getItem(filepath) ?: return emptyList()
        return json.parse(DirectoryInfo.serializer(), string).fileItems
    }

    actual fun getFileListAsync(filepath: String): Deferred<List<FileItem>> {
        return Promise<List<FileItem>> { resolve, _ ->
            resolve(getFileList(filepath))
        }.asDeferred()
    }

    actual fun makeDirectory(writablePath: String): Boolean {
        check(writablePath.startsWith("@ls://")) {
            "This file path cannot be written [$writablePath]"
        }
        val directories = writablePath
                .removePrefix("@ls://")
                .split("/")
                .dropLast(1)

        var paths = "@ls://"
        for (directory in directories) {
            paths = Path.resolve(paths, directory)
            if (getWritableDirectoryInfo(paths) == null) {
                return false
            }
        }
        if (getWritableDirectoryInfo(writablePath) == null) {
            localStorage.setItem(
                    writablePath,
                    json.stringify(DirectoryInfo.serializer(), DirectoryInfo(mutableListOf()))
            )
        }
        return true
    }

    actual fun makeDirectories(writablePath: String): Boolean {
        check(writablePath.startsWith("@ls://")) {
            "This file path cannot be written [$writablePath]"
        }
        val directories = writablePath
                .removePrefix("@ls://")
                .split("/")
                .dropLast(1)

        var paths = "@ls://"
        for (directory in directories) {
            paths = Path.resolve(paths, directory)
            if (getWritableDirectoryInfo(paths) == null) {
                localStorage.setItem(writablePath, json.stringify(DirectoryInfo.serializer(), DirectoryInfo(mutableListOf())))
            }
        }
        if (getWritableDirectoryInfo(writablePath) == null) {
            localStorage.setItem(
                    writablePath,
                    json.stringify(DirectoryInfo.serializer(), DirectoryInfo(mutableListOf()))
            )
        }
        return true
    }

    private fun getWritableDirectoryInfo(directory: String): DirectoryInfo? {
        val string = localStorage.getItem(Path.resolve(directory, ".")) ?: return null
        return try {
            return json.parse(DirectoryInfo.serializer(), string)
        } catch (e: Error) {
            null
        }
    }
}
