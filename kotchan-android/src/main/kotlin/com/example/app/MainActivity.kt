package com.example.app

import android.content.res.AssetManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle



class MainActivity : AppCompatActivity() {
    companion object {
        private var assetManager: AssetManager? = null
        fun getAssets() = assetManager
    }


    private var mySurfaceView: SurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        assetManager = assets
        mySurfaceView = SurfaceView(this)
        super.onCreate(savedInstanceState)
        setContentView(mySurfaceView)
    }
}
