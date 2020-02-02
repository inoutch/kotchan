package io.github.inoutch.kotchan.core.io.file

data class NoSuchDirectoryError(val directoryPath: String) :
    FileError("No such file error [directory path = $directoryPath]")
