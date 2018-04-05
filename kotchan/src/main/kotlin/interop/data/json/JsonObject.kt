package interop.data.json

class JsonObject {
    enum class JsonType {
        ErrorType,
        FloatType,
        MapType,
        ListType,
        StringType,
    }

    private var type: JsonType = JsonType.ErrorType

    private var floatValue: Float? = null
    private var textValue: String? = null
    private var listValue: MutableList<JsonObject> = mutableListOf()
    private var mapValue: MutableMap<String, JsonObject> = mutableMapOf()

    constructor(value: Float) {
        floatValue = value
        type = JsonType.FloatType
    }

    constructor(value: String) {
        textValue = value
        type = JsonType.StringType
    }

    private constructor(type: JsonType) {
        this.type = type
    }

    companion object {
        fun createList() = JsonObject(JsonType.ListType)
        fun createMap() = JsonObject(JsonType.MapType)
    }

    fun isFloat() = type == JsonType.FloatType
    fun isList() = type == JsonType.ListType
    fun isMap() = type == JsonType.MapType

    fun toFloat() = floatValue
    fun toMap() = mapValue
    fun toList() = listValue
    fun toText() = textValue

    fun addAsMap(key: String, jsonObject: JsonObject) {
        if (type == JsonType.MapType) mapValue[key] = jsonObject
    }

    fun addAsList(jsonObject: JsonObject) {
        if (type == JsonType.ListType) listValue.add(jsonObject)
    }

    fun setFloat(value: Float) {
        if (isFloat()) floatValue = value
    }
}