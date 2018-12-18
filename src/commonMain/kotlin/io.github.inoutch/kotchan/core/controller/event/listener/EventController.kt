package io.github.inoutch.kotchan.core.controller.event.listener

class EventController {

    val events: MutableMap<Any, MutableMap<String, MutableList<(Any) -> Unit>>> = mutableMapOf()

    inline fun <reified T> subscribe(id: Any, name: String, crossinline callback: (event: T) -> Unit) {
        val eventHandler = { event: Any ->
            if (event is T) {
                callback(event)
            }
        }
        events.getOrPut(id) { mutableMapOf() }
                .getOrPut(name) { mutableListOf() }
                .add(eventHandler)
    }

    fun publish(id: Any, name: String, event: Any) {
        events[id]?.get(name)?.forEach { it.invoke(event) }
    }

    fun clear() {
        events.clear()
    }
}