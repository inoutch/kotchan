package interop.data.json

import extension.*

class JsonObject {

    private var type: JsonType = JsonType.ErrorType

    private var floatValue: Float? = null
    private var textValue: String? = null
    private var listValue = mutableListOf<JsonObject>()
    private var mapValue = mutableMapOf<String, JsonObject>()

    constructor(value: Boolean) {
        floatValue = if (value) 1.0f else 0.0f
        type = JsonType.FloatType
    }

    constructor(value: Float) {
        floatValue = value
        type = JsonType.FloatType
    }

    constructor(value: String) {
        textValue = value
        type = JsonType.TextType
    }

    constructor(value: Int) {
        floatValue = value.toFloat()
        type = JsonType.FloatType
    }

    constructor(vararg items: JsonObject) {
        this.type = JsonType.ListType
        listValue.addAll(items)
    }

    private constructor(type: JsonType) {
        this.type = type
    }

    companion object {
        fun createList() = JsonObject(JsonType.ListType)
        fun createMap() = JsonObject(JsonType.MapType)
    }

    // byScheme check
    fun isFloat() = type == JsonType.FloatType

    fun isList() = type == JsonType.ListType

    fun isMap() = type == JsonType.MapType

    fun isText() = type == JsonType.TextType

    fun hasKeys(keys: List<Pair<String, JsonType>>) =
            isMap() && !keys.any { mapValue[it.first]?.type != it.second }

    fun byScheme(keys: List<Pair<String, JsonType>>): List<JsonObject>? {
        if (!hasKeys(keys)) return null
        return mapValue[keys.map { it.first }]
    }

    fun toFloat() = floatValue

    fun toInt() = floatValue?.toInt()

    fun toBoolean(): Boolean? {
        return (floatValue ?: return null) > 0.5f
    }

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