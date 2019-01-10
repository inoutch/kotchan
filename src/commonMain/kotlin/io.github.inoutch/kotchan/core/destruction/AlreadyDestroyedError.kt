package io.github.inoutch.kotchan.core.destruction

import kotlin.reflect.KClass

class AlreadyDestroyedError(kClass: KClass<*>) : Error("${kClass.simpleName} has already destroyed")