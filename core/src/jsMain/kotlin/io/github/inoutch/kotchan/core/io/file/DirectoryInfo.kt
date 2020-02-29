package io.github.inoutch.kotchan.core.io.file

import kotlinx.serialization.Serializable

@Serializable
data class DirectoryInfo(
        val fileItems: MutableList<FileItem>
)
