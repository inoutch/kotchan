package io.github.inoutch.kotchan.extension

fun String.splitWithEscaping(border: Char): List<String> {
    var i = 0
    val items = mutableListOf<String>()
    val chars = mutableListOf<Char>()
    var qEscape = false
    var sEscape = false

    while (i < this.length) {
        if (qEscape) {
            sEscape = this[i] == '\\'
            if (sEscape) {
                i++
            }
        }
        when {
            this[i] == '"' && !sEscape -> {
                qEscape = !qEscape
                chars.add(this[i])
            }
            this[i] == border && !qEscape -> {
                items.add(String(chars.toCharArray()))
                chars.clear()
            }
            else -> {
                chars.add(this[i])
            }
        }
        i++
    }
    items.add(String(chars.toCharArray()))
    return items
}