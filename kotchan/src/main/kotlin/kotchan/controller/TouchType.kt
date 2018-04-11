package kotchan.controller

enum class TouchType {
    None,
    Began,
    BeganIdle,
    Moved,
    MovedIdle,
    Ended,
    BeganAndMoved,
    BeganAndEnded,
    BeganAndMovedAndEnded,
    Cancelled
}

fun TouchType.likeBegan(): Boolean = when (this) {
    TouchType.Began,
    TouchType.BeganAndEnded,
    TouchType.BeganAndMoved,
    TouchType.BeganAndMovedAndEnded -> true
    else -> false
}

fun TouchType.likeEnded(): Boolean = when (this) {
    TouchType.Ended,
    TouchType.BeganAndEnded,
    TouchType.BeganAndMovedAndEnded -> true
    else -> false
}