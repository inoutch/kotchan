package io.github.inoutch.kotchan.core.platform

import android.content.res.AssetManager
import io.github.inoutch.kotchan.core.io.file.FileItem
import io.github.inoutch.kotchan.core.io.file.FileType
import io.github.inoutch.kotchan.utility.Path
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class KotchanFilePlatform(
        private val assetManager: AssetManager,
        private val fileDirectory: String
) {
    companion object {
        const val ASSET_PREFIX = "@asset:"
    }

    fun inputStream(filepath: String) = if (filepath.startsWith(ASSET_PREFIX)) {
        assetManager.open(filepath.removePrefix(ASSET_PREFIX))
    } else {
        FileInputStream(File(filepath))
    }

    fun outputStream(filepath: String) = FileOutputStream(File(filepath))

    fun filesDir(name: String) = Path.resolve(*listOfNotNull(fileDirectory, name).toTypedArray())

    fun fileList(filepath: String): List<FileItem> {
        return if (filepath.startsWith(ASSET_PREFIX)) {
            val writeFilepath = filepath.removePrefix(ASSET_PREFIX)
            assetManager.list(writeFilepath)?.map {
                FileItem(it, fileTypeForAssets(Path.resolve(writeFilepath, it)))
            } ?: emptyList()
        } else {
            File(filepath).listFiles().map {
                FileItem(it.name, if (it.isFile) FileType.File else FileType.Directory)
            }
        }
    }

    fun assetFileDescriptor(filepath: String) = if (filepath.startsWith(ASSET_PREFIX)) {
        assetManager.openFd(filepath.removePrefix(ASSET_PREFIX))
    } else {
        null
    }

    private fun fileTypeForAssets(filepath: String) = try {
        assetManager.open(filepath)
        FileType.File
    } catch (e: IOException) {
        FileType.Directory
    }
}
