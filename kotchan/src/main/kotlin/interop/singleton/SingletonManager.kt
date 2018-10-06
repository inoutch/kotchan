package interop.singleton

class SingletonManager {
    private val instances = HashMap<String, Any>()

    fun add(name: String, instance: Any) {
        instances[name] = instance
    }

    fun get(name: String): Any? {
        return instances[name]
    }
}