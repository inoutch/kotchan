package io.github.inoutch.kotchan.core.error

class NoSuchFileError(file: String) : Error("$file: no such file")