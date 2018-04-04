package interop.data.json

expect class Json {
    companion object {
        fun parse(json: String): JsonObject
        fun write(jsonObject: JsonObject): String
    }
}