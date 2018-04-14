package application

import kotchan.constant.ScreenType
import utility.type.Vector2

class AppConfig {
    companion object {
        const val APP_NAME = "kotchan"
        val SCREEN_SIZE = Vector2(640.0f, 1136.0f) / 4.0f
        val WINDOW_SIZE = Vector2(640.0f, 1336.0f)
        val SCREEN_TYPE = ScreenType.BORDER
    }
}