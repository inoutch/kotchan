package io.github.inoutch.kotchan.core.platform

import android.app.Activity
import android.os.Bundle
import io.github.inoutch.kotchan.core.KotchanPlatformConfig
import io.github.inoutch.kotchan.core.KotchanStartupConfig

abstract class KotchanActivity : Activity() {

    abstract val startupConfig: KotchanStartupConfig

    open val platformConfig: KotchanPlatformConfig? = null

    private lateinit var surfaceView: KotchanGLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        surfaceView = KotchanGLSurfaceView(startupConfig, platformConfig, this.applicationContext)
        setContentView(surfaceView)
    }

    override fun onPause() {
        super.onPause()
        surfaceView.onPause()
    }

    override fun onResume() {
        super.onResume()
        surfaceView.onResume()
    }
}
