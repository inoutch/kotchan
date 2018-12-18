package io.github.inoutch.kotchan.ios

import platform.UIKit.*

class AppDelegate : UIResponder, UIApplicationDelegateProtocol {

    companion object : UIResponderMeta(), UIApplicationDelegateProtocolMeta

    private var _window: UIWindow? = null

    @OverrideInit
    constructor() : super()

    override fun window() = _window

    override fun setWindow(window: UIWindow?) {
        _window = window
    }
}