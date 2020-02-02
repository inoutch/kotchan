package io.github.inoutch.kotchan.core.io.file

import kotlinx.coroutines.Deferred

actual class File actual constructor() {
    actual fun readBytes(filepath: String): ByteArray? {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    actual fun readBytesAsync(filepath: String): Deferred<ByteArray?> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    actual fun writeBytes(writableFilepath: String, bytes: ByteArray): Boolean {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    actual fun writeBytesAsync(writableFilepath: String, bytes: ByteArray): Deferred<Boolean> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    actual fun getResourcePath(name: String): String? {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    actual fun getWritablePath(name: String): String {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    actual fun getFileList(filepath: String): List<FileItem> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    actual fun getFileListAsync(filepath: String): Deferred<List<FileItem>> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    actual fun makeDirectory(writablePath: String): Boolean {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    actual fun makeDirectories(writablePath: String): Boolean {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
