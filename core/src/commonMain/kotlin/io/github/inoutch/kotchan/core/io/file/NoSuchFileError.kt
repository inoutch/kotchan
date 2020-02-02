package io.github.inoutch.kotchan.core.io.file

data class NoSuchFileError(val filePath: String) :
    FileError("No such file error [file path = $filePath]")
