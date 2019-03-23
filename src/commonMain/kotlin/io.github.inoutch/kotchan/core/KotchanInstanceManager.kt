package io.github.inoutch.kotchan.core

class KotchanInstanceManager {

    private val instances = HashMap<String, Any>()

    fun add(name: String, instance: Any) {
        instances[name] = instance
    }

    fun get(name: String): Any? {
        return instances[name]
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getOrAdd(name: String, addCallback: () -> T): T {
        return get(name) as T? ?: addCallback().also { add(name, it) }
    }
}
