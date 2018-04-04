package interop.data.json

expect class JsonObject {
    fun toFloat(): Float?
    fun toInt(): Float?
    fun toMap(): Map<String, JsonObject>
    fun toArray(): Array<JsonObject>

    fun isFloat(): Boolean
    fun isInt(): Boolean
    fun isArray(): Boolean

    fun addAsMap(key: String, value: JsonObject)
    fun addAsArray(key: String, value: JsonObject)

    fun setFloat(value: Float)
    fun setInt(value: Int)
}