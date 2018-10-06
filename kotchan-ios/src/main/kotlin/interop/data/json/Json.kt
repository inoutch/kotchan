package interop.data.json

import kotlinx.cinterop.*
import platform.CoreFoundation.*
import platform.Foundation.*

import extension.*
import kotchan.logger.*

actual class Json {
    actual companion object {
        actual fun parse(json: String): JsonObject? {
            val data = json.toNSString().dataUsingEncoding(NSUTF8StringEncoding) ?: return null
            val jsonObject = memScoped {
                val errorVar = alloc<ObjCObjectVar<NSError?>>()
                val result = NSJSONSerialization.JSONObjectWithData(data, 0, errorVar.ptr)
                if (errorVar.value != null) {
                    return null
                }
                result!!
            }
            return parseNative(jsonObject)
        }

        actual fun write(jsonObject: JsonObject): String? {
            val obj = when {
                jsonObject.isMap() -> output(jsonObject)
                jsonObject.isList() -> output(jsonObject)
                else -> null
            } ?: return null
            val data = NSJSONSerialization.dataWithJSONObject(obj, 0, null) ?: return null
            return data.bytes!!.readBytes(data.length.toInt()).stringFromUtf8()
        }

        private fun output(obj: JsonObject): Any? {
            when {
                obj.isFloat() -> return obj.toFloat()
                obj.isText() -> return obj.toText()
                obj.isList() -> return obj.toList().mapNotNull { output(it) }
                obj.isMap() -> {
                    val mutableMap = mutableMapOf<String, Any>()
                    obj.toMap().forEach {
                        mutableMap[it.key] = output(it.value) ?: return@forEach
                    }
                    return mutableMap
                }
            }
            throw Error("output undefined type")
        }

        @Suppress("UNCHECKED_CAST")
        private fun parseNative(value: Any): JsonObject {
            if (value is Boolean) {
                return JsonObject(if (value) 1.0f else 0.0f)
            } else if (value is Number) {
                return JsonObject(value.toFloat())
            } else if (value is String) {
                return JsonObject(value.toString())
            } else if (value is Map<*, *>) {
                val map = value as Map<String, Any>
                return JsonObject.createMap().also { j ->
                    value.forEach { j.addAsMap(it.key, parseNative(it.value)) }
                }
            } else if (value is List<*>) {
                val array = value as List<Any>
                return JsonObject.createList().also { j ->
                    value.forEach { j.addAsList(parseNative(it)) }
                }
            } else {
                logger.error("invalid json object is " + value)
                throw Error("unknown json extension")
            }
        }
    }
}