package io.github.inoutch.kotchan.core.graphic.map.converter

import io.github.inoutch.kotchan.utility.data.json.Json
import interop.data.json.JsonType
import io.github.inoutch.kotchan.core.KotchanCore
import io.github.inoutch.kotchan.core.logger.logger
import io.github.inoutch.kotchan.utility.type.Point

class TileConverter {

    private val converters: MutableMap<String, List<ConversionData>> = mutableMapOf()

    fun load(filepath: String, name: String) {
        val json = KotchanCore.instance.file.readText(filepath)
        if (json == null) {
            logger.error("$filepath is not found.")
            return
        }
        val jsonObject = Json.parse(json)
        if (jsonObject == null) {
            logger.error("could not parse $filepath.")
            return
        }
        if (!jsonObject.isList()) {
            logger.error("the root of $filepath is not array.")
            return
        }

        val converterScheme = listOf(
                Pair("in", JsonType.ListType),
                Pair("out", JsonType.MapType))
        val outputScheme = listOf(
                Pair("x", JsonType.FloatType),
                Pair("y", JsonType.FloatType),
                Pair("v", JsonType.FloatType))

        converters[name] = jsonObject.toList()
                .filter {
                    val result = it.hasKeys(converterScheme)
                    if (!result) {
                        logger.warn("[$filepath] converter does not have \"in\" and \"out\".")
                    }
                    result
                }
                .mapNotNull {
                    val input = it.toMap()["in"] ?: return@mapNotNull null
                    val output = it.toMap()["out"] ?: return@mapNotNull null

                    val first = input.toList().firstOrNull()
                    if (first == null || !first.isList()) {
                        logger.warn("[$filepath] converter does not have \"in\" array.")
                        return@mapNotNull null
                    }
                    val (y, x) = input.toList().size to first.toList().size
                    val sizeError = input.toList().any { x != it.toList().size }
                    if (sizeError) {
                        logger.warn("[$filepath] converter does not have \"in\" array of invalid size.")
                        return@mapNotNull null
                    }
                    val values = input.toList()
                            .map { it.toList().map { it.toFloat()?.toInt() ?: -1 } }
                            .reversed()

                    if (!output.hasKeys(outputScheme)) {
                        logger.warn("[$filepath] out is invalid scheme.")
                        return@mapNotNull null
                    }
                    val ox = output.toMap()["x"]?.toFloat()?.toInt() ?: return@mapNotNull null
                    val oy = output.toMap()["y"]?.toFloat()?.toInt() ?: return@mapNotNull null
                    val ov = output.toMap()["v"]?.toFloat()?.toInt() ?: return@mapNotNull null
                    if (ox < -1 || ox >= x || oy < -1 || oy >= y) {
                        logger.warn("[$filepath] x and y of out is invalid.")
                        return@mapNotNull null
                    }
                    ConversionData(
                            ConversionInputData(values, x, y),
                            ConversionOutputData(ox, oy, ov))
                }
    }

    fun convert(name: String, point: Point, input: (point: Point) -> Int, output: (point: Point, id: Int) -> Unit) {
        val converters = this.converters[name] ?: return
        for (converter in converters) {
            val sx = point.x - converter.output.x
            val sy = point.y - converter.output.y
            var flag = true
            for (my in 0 until converter.input.sizeY) {
                for (mx in 0 until converter.input.sizeX) {
                    val a = input(Point(mx + sx, my + sy))
                    val b = converter.input.input[my][mx]
                    if (b != -1 && a != b) {
                        flag = false
                        break
                    }
                }
            }
            if (flag) {
                output(point, converter.output.v)
                return
            }
        }
    }
}
