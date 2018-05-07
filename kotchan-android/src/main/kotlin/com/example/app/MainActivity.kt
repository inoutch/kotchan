package com.example.app

import android.content.res.AssetManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    companion object {
        private var assetManager: AssetManager? = null
        fun getAssets() = assetManager
    }

    private lateinit var surfaceView: SurfaceView
    override fun onCreate(savedInstanceState: Bundle?) {
        assetManager = assets
        surfaceView = SurfaceView(this)
        super.onCreate(savedInstanceState)
        setContentView(surfaceView)
    }
}
