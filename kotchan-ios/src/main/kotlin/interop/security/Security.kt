package interop.security

actual class Security {
    actual companion object {
        actual fun hash(input: String): String {
            return "Not implemented"
        }
    }
}