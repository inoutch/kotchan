package interop.data.json

class JsonObject {
    enum class JsonType {
        ErrorType,
        FloatType,
        MapType,
        ListType,
        TextType,
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
        type = JsonType.TextType
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
    fun isText() = type == JsonType.TextType

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
        if (!isFloat()) {
            setNull(type)
            type = JsonType.FloatType
        }
        floatValue = value
    }

    fun setText(value: String) {
        if (!isText()) {
            setNull(type)
            type = JsonType.TextType
        }
        textValue = value
    }

    fun setMap() {
        if (!isMap()) {
            setNull(type)
        }
        mapValue.clear()
    }

    fun setList() {
        if (!isList()) {
            setNull(type)
        }
        listValue.clear()
    }

    private fun setNull(type: JsonType) {
        when (type) {
            JsonType.FloatType -> {
                floatValue = null
            }
            JsonType.MapType -> {
                mapValue = mutableMapOf()
            }
            JsonType.ListType -> {
                listValue = mutableListOf()
            }
            JsonType.TextType -> {
                textValue = null
            }
            else -> throw Error("unknown type")
        }
    }
}