package  kotchan

import android.app.Activity
import android.content.res.AssetManager
import android.os.Bundle
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class MainActivity : Activity() {
    companion object {
        private var assetManager: AssetManager? = null
        fun getAssets() = assetManager
        fun getInputStream(filepath: String): InputStream? {
            return if (filepath.startsWith("@assets:")) {
                assetManager?.open(filepath.removePrefix("@assets:"))
            } else {
                FileInputStream(File(filepath))
            }
        }
    }

    private lateinit var surfaceView: SurfaceView
    override fun onCreate(savedInstanceState: Bundle?) {
        assetManager = assets
        surfaceView = SurfaceView(this)
        super.onCreate(savedInstanceState)
        setContentView(surfaceView)
    }
}
