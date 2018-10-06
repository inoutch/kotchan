package interop.singleton

actual class Singleton {
    @kotlin.native.ThreadLocal
    actual companion object {
        actual fun getInstance(): SingletonManager {
            return singletonManager
        }

        private val singletonManager = SingletonManager()
    }
}