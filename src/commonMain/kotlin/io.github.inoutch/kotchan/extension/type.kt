package io.github.inoutch.kotchan.extension

fun <T> T?.getOrCreate(put: () -> T): T {
    val target = this
    if (target != null) {
        return target
    }
    return put()
}
