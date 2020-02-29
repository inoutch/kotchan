package io.github.inoutch.kotchan.core.io.file

import kotlinx.serialization.Serializable

@Serializable
enum class FileType {
    File,
    Directory,
}
