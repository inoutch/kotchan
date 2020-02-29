package io.github.inoutch.kotchan.core.io.file

import kotlinx.serialization.Serializable

@Serializable
class FileItem(val name: String, val fileType: FileType)
