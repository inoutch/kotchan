package io.github.inoutch.kotchan.utility.io

import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.error.NoSuchFileError

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

class NoSuchDirectoryError(e: String) : Error("$e: no such directory")

fun File.readTextFromResource(filepath: String) =
        KotchanCore.instance.file.getResourcePath(filepath)?.let { KotchanCore.instance.file.readText(it) }

fun File.readBytesFromResource(filepath: String) =
        KotchanCore.instance.file.getResourcePath(filepath)?.let { KotchanCore.instance.file.readText(it) }

fun File.readTextFromResourceWithError(filepath: String) =
        readTextFromResource(filepath) ?: throw NoSuchFileError(filepath)

fun File.readBytesFromResourceWithError(filepath: String) =
        readBytesFromResource(filepath) ?: throw NoSuchFileError(filepath)

fun File.getResourcePathWithError(name: String) = getResourcePath(name) ?: throw NoSuchDirectoryError(name)

