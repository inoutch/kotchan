package io.github.inoutch.kotchan.android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.AssetManager
import android.os.Bundle
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import io.github.inoutch.kotchan.core.KotchanEngine
import io.github.inoutch.kotchan.utility.path.Path
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import android.widget.FrameLayout

abstract class KotchanActivity : Activity() {

    companion object {
        private const val ASSET_PREFIX = "@assets:"

        private var assetManager: AssetManager? = null

        private var fileDirectory: String? = null

        @SuppressLint("StaticFieldLeak")
        var keyboard: KotchanKeyboard? = null
            private set

        fun inputStream(filepath: String) = if (filepath.startsWith(ASSET_PREFIX)) {
            assetManager?.open(filepath.removePrefix(ASSET_PREFIX))
        } else {
            FileInputStream(File(filepath))
        }

        fun outputStream(filepath: String) = FileOutputStream(File(filepath))

        fun filesDir(name: String) = Path.resolve(*listOfNotNull(fileDirectory, name).toTypedArray())

        fun assetFileDescriptor(filepath: String) = if (filepath.startsWith(ASSET_PREFIX)) {
            assetManager?.openFd(filepath.removePrefix(ASSET_PREFIX))
        } else {
            null
        }
    }

    private lateinit var surfaceView: KotchanSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        surfaceView = KotchanSurfaceView(config(), this)

        super.onCreate(savedInstanceState)
        setContentView(surfaceView)

        assetManager = assets
        fileDirectory = applicationContext.filesDir.path
        val inputMethodManager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val rootLayout = findViewById<FrameLayout>(android.R.id.content)
        val editText = EditText(applicationContext)
        editText.layoutParams = ViewGroup.LayoutParams(0, 0)
        rootLayout.addView(editText)
        keyboard = KotchanKeyboard(editText, inputMethodManager)
    }

    override fun onPause() {
        super.onPause()
        surfaceView.onPause()
    }

    override fun onResume() {
        super.onResume()
        surfaceView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        assetManager = null
        keyboard = null
    }

    abstract fun config(): KotchanEngine.Config
}
