package interop.data.json

class JsonObject {
    enum class JsonType {
        ErrorType,
        FloatType,
        IntType,
        MapType,
        ListType,
        BooleanType,
    }

    private var type: JsonType = JsonType.ErrorType

    private var intValue: Int? = null
    private var floatValue: Float? = null
    private var booleanValue: Boolean? = null
    private var listValue: MutableList<JsonObject> = mutableListOf()
    private var mapValue: MutableMap<String, JsonObject> = mutableMapOf()

    constructor(value: Int) {
        intValue = value
        type = JsonType.IntType
    }

    constructor(value: Float) {
        floatValue = value
        type = JsonType.FloatType
    }

    constructor(value: Boolean) {
        booleanValue = value
        type = JsonType.BooleanType
    }

    private constructor(type: JsonType) {
        this.type = type
    }

    companion object {
        fun createList() = JsonObject(JsonType.ListType)
        fun createMap() = JsonObject(JsonType.MapType)
    }

    fun isFloat() = type == JsonType.FloatType
    fun isInt() = type == JsonType.IntType
    fun isList() = type == JsonType.ListType
    fun isMap() = type == JsonType.MapType
    fun isBoolean() = type == JsonType.BooleanType

    fun toFloat() = floatValue
    fun toInt() = intValue
    fun toMap() = mapValue
    fun toList() = listValue
    fun toBoolean() = booleanValue

    fun addAsMap(key: String, jsonObject: JsonObject) {
        if (type == JsonType.MapType) mapValue[key] = jsonObject
    }

    fun addAsList(jsonObject: JsonObject) {
        if (type == JsonType.ListType) listValue.add(jsonObject)
    }

    fun setInt(value: Int) {
        if (isInt()) intValue = value
    }

    fun setFloat(value: Float) {
        if (isFloat()) floatValue = value
    }
}