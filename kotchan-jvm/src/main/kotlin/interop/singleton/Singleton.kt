package interop.singleton

actual class Singleton {
    actual companion object {
        actual fun getInstance(): SingletonManager {
            return singletonManager
        }

        private val singletonManager = SingletonManager()
    }
}