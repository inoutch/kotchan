package interop.data.json

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

actual class Json {
    actual companion object {
        private val mapper = jacksonObjectMapper()
        actual fun parse(json: String): JsonObject {
            return parse(mapper.readTree(json))
        }

        actual fun write(jsonObject: JsonObject): String {
            return "[]"
        }

        private fun parse(jsonNode: JsonNode): JsonObject {
            when {
                jsonNode.isInt -> return JsonObject(jsonNode.intValue())
                jsonNode.isFloat -> return JsonObject(jsonNode.floatValue())
                jsonNode.isBoolean -> return JsonObject(jsonNode.booleanValue())
                jsonNode.isDouble -> return JsonObject(jsonNode.doubleValue().toFloat())
                jsonNode.isObject -> {
                    val jsonObject = JsonObject.createMap()
                    jsonNode.fields().forEach {
                        jsonObject.addAsMap(it.key, parse(it.value))
                    }
                    return jsonObject
                }
                jsonNode.isArray -> {
                    val jsonObject = JsonObject.createList()
                    for (node in jsonNode) {
                        jsonObject.addAsList(parse(node))
                    }
                    return jsonObject
                }
                else -> throw Error("unknown json type")
            }
        }
    }
}