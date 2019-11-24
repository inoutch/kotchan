package io.github.inoutch.kotchan.utility.data.json

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

actual class Json {
    actual companion object {
        private val mapper = jacksonObjectMapper()

        actual fun parse(json: String) = try {
            parse(mapper.readTree(json))
        } catch (e: JsonParseException) {
            null
        }

        actual fun write(jsonObject: JsonObject) = try {
            mapper.writeValueAsString(outputNode(jsonObject))
        } catch (e: JsonProcessingException) {
            null
        }

        private fun outputNode(obj: JsonObject): JsonNode {
            when {
                obj.isList() -> return mapper.createArrayNode().also { node ->
                    obj.toList().forEach {
                        when {
                            it.isFloat() -> node.add(it.toFloat())
                            it.isText() -> node.add(it.toText())
                            else -> node.add(outputNode(it))
                        }
                    }
                }
                obj.isMap() -> return mapper.createObjectNode().also { node ->
                    obj.toMap().forEach {
                        when {
                            it.value.isFloat() -> node.put(it.key, it.value.toFloat())
                            it.value.isText() -> node.put(it.key, it.value.toText())
                            else -> node[it.key] = outputNode(it.value)
                        }
                    }
                }
                else -> throw Error("broken json object")
            }
        }

        private fun parse(jsonNode: JsonNode): JsonObject {
            when {
                jsonNode.isInt -> return JsonObject(jsonNode.intValue().toFloat())
                jsonNode.isFloat -> return JsonObject(jsonNode.floatValue())
                jsonNode.isBoolean -> return JsonObject(if (jsonNode.booleanValue()) 1.0f else 0.0f)
                jsonNode.isDouble -> return JsonObject(jsonNode.doubleValue().toFloat())
                jsonNode.isTextual -> return JsonObject(jsonNode.textValue())
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
                else -> throw Error("unknown json extension")
            }
        }
    }
}
