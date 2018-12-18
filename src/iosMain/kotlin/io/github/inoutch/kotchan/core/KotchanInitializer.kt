package io.github.inoutch.kotchan.core


import io.github.inoutch.kotchan.ios.AppDelegate
import io.github.inoutch.kotchan.ios.DefaultConfig
import kotlinx.cinterop.*
import platform.Foundation.NSStringFromClass
import platform.UIKit.UIApplicationMain

actual class KotchanInitializer {
    actual companion object {
        actual fun initialize(config: KotchanEngine.Config) {
            DefaultConfig.config = config

            memScoped {
                val argc = 0
                val argv = emptyList<String>().toCStringArray(memScope)

                autoreleasepool {
                    UIApplicationMain(argc, argv, null, NSStringFromClass(AppDelegate))
                }
            }
        }
    }
}