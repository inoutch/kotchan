package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.ios.DefaultConfig
import kotlinx.cinterop.*
import platform.Foundation.NSStringFromClass
import platform.UIKit.UIApplicationMain

fun main(args: Array<String>) {
    DefaultConfig.config = AppConfig()
    memScoped {
        autoreleasepool {
            UIApplicationMain(args.size, args.toCStringArray(this), null, NSStringFromClass(AppDelegate))
        }
    }
}
