package kotchan.view.map.converter

import interop.data.json.Json
import interop.data.json.JsonType
import kotchan.Engine
import kotchan.logger.logger
import kotchan.view.map.TileLayerBase

class TileConverter {
    private val converters: MutableMap<String, List<ConversionData>> = mutableMapOf()

    fun load(filepath: String, name: String) {
        val json = Engine.getInstance().file.readText(filepath)
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

    fun convert(name: String, x: Int, y: Int, inLayer: TileLayerBase, outLayer: TileLayerBase) {
        val converters = this.converters[name] ?: return
        for (converter in converters) {
            val sx = x - converter.output.x
            val sy = y - converter.output.y
            var flag = true
            for (my in 0 until converter.input.sizeY) {
                for (mx in 0 until converter.input.sizeX) {
                    val a = inLayer.mapId(mx + sx, my + sy) ?: 0
                    val b = converter.input.input[my][mx]
                    if (b != -1 && a != b) {
                        flag = false
                        break
                    }
                }
            }
            if (flag) {
                outLayer.mapId(x, y, converter.output.v)
                return
            }
        }
    }
}