package  kotchan

import android.app.Activity
import android.content.res.AssetManager
import android.os.Bundle
import utility.path.Path
import java.io.*

class MainActivity : Activity() {
    companion object {
        private var assetManager: AssetManager? = null
        private var fileDirectory: String? = null

        fun getInputStream(filepath: String) = if (filepath.startsWith("@assets:")) {
            assetManager?.open(filepath.removePrefix("@assets:"))
        } else {
            FileInputStream(File(filepath))
        }

        fun getOutputStream(filepath: String) = FileOutputStream(File(filepath))

        fun getFilesDir(name: String) = Path.resolve(*listOfNotNull(fileDirectory, name).toTypedArray())
    }

    private lateinit var surfaceView: SurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        surfaceView = SurfaceView(this)

        super.onCreate(savedInstanceState)
        setContentView(surfaceView)

        assetManager = assets
        fileDirectory = applicationContext.filesDir.path

    }
}
