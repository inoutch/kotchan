package io.github.inoutch.kotchan.ios

import platform.UIKit.UIViewController

class KotchanViewContext(val viewController: UIViewController) {
    @ThreadLocal
    companion object {
        private var currentViewContext: KotchanViewContext? = null

        val viewContext: KotchanViewContext
            get() = currentViewContext ?: throw IllegalStateException("currentViewContext is null")

        fun register(viewContext: KotchanViewContext) {
            currentViewContext = viewContext
        }
    }
}
