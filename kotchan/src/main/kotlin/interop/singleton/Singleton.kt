package interop.singleton

expect class Singleton {
    companion object {
        fun getInstance(): SingletonManager
    }
}