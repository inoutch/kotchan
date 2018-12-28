package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.android.KotchanActivity
import AppConfig

class MainActivity : KotchanActivity() {
    override fun config() = AppConfig()
}