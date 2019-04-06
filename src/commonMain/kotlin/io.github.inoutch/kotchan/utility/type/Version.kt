package io.github.inoutch.kotchan.utility.type

import kotlinx.serialization.Serializable

@Serializable
data class Version(val major: Int, val minor: Int, val patch: Int)
