package interop.data.json

expect class JsonParser {
    companion object {
        fun parse(json: String): JsonObject
    }
}