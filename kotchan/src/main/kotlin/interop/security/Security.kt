package interop.security

expect class Security {
    companion object {
        fun hash(input: String): String
    }
}