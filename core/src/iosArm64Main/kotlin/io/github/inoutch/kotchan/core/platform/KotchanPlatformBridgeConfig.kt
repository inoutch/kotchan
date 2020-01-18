package io.github.inoutch.kotchan.core.platform

import io.github.inoutch.kotchan.core.KotchanPlatformConfig

class KotchanPlatformBridgeConfig @ExperimentalUnsignedTypes constructor(
        val viewController: KotchanViewController,
        val viewControllerConfig: KotchanViewControllerConfig
) : KotchanPlatformConfig
