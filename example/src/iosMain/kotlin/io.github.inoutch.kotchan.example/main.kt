package io.github.inoutch.kotchan.example

import kotlinx.cinterop.*
import platform.Foundation.NSStringFromClass
import platform.UIKit.UIApplicationMain

fun main(args: Array<String>) {
    memScoped {
        autoreleasepool {
            UIApplicationMain(args.size, args.toCStringArray(this), null, NSStringFromClass(AppDelegate))
        }
    }
}
