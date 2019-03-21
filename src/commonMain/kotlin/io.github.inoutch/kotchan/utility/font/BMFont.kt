package io.github.inoutch.kotchan.utility.font

import io.github.inoutch.kotchan.core.KotchanCore.Companion.instance
import io.github.inoutch.kotchan.core.KotchanCore.Companion.logger
import io.github.inoutch.kotchan.core.error.NoSuchFileError
import io.github.inoutch.kotchan.core.graphic.Material
import io.github.inoutch.kotchan.core.graphic.texture.Texture
import io.github.inoutch.kotchan.extension.splitWithEscaping
import io.github.inoutch.kotchan.utility.Disposable
import io.github.inoutch.kotchan.utility.io.getResourcePathWithError
import io.github.inoutch.kotchan.utility.io.readTextFromResource
import io.github.inoutch.kotchan.utility.path.Path
import io.github.inoutch.kotchan.utility.type.Point
import io.github.inoutch.kotchan.utility.type.PointRect

class BMFont private constructor(
        textureDir: String,
        materialConfig: Material.Config,
        val info: Info,
        val common: Common,
        val pages: List<Page>,
        val chars: Map<Int, Char>,
        // first char id, second char id, amount
        val kernings: Map<Int, Map<Int, Int>>) : Disposable {

    companion object {
        fun load(filepath: String, textureDir: String, materialConfig: Material.Config) =
                instance.file.readText(filepath)?.let { parse(it, textureDir, materialConfig) }

        fun loadFromResource(filepath: String, textureDir: String, materialConfig: Material.Config) =
                instance.file.readTextFromResource(filepath)?.let {
                    parse(it, instance.file.getResourcePathWithError(textureDir), materialConfig)
                } ?: throw NoSuchFileError(filepath)

        fun parse(text: String, textureDir: String, materialConfig: Material.Config): BMFont {
            val lines = text.split("\n")
            val chunks = lines
                    .mapNotNull { line -> convertChunk(line.splitWithEscaping(' ').filter { it.isNotEmpty() }) }
                    .groupBy { it.type }

            val info = cast<Info>(chunks[ChunkType.INFO])?.firstOrNull()
                    ?: throw InvalidFormat("info is not found")
            val common = cast<Common>(chunks[ChunkType.COMMON])?.firstOrNull()
                    ?: throw InvalidFormat("common is not found")
            val pages = cast<Page>(chunks[ChunkType.PAGE])
                    ?: throw InvalidFormat("page is not found")
            val chars = cast<Char>(chunks[ChunkType.CHAR])
                    ?: throw InvalidFormat("char is not found")
            val kernigs = cast<Kerning>(chunks[ChunkType.KERNING])
                    ?: emptyList()

            if (cast<Chars>(chunks[ChunkType.CHARS])?.firstOrNull()?.count != chars.size) {
                logger.warn("The count of 'char' is different from 'chars")
            }

            if (cast<Kernings>(chunks[ChunkType.KERNINGS])?.firstOrNull()?.count != kernigs.size) {
                logger.warn("The count of 'kerning' is different from 'kernings")
            }

            return BMFont(
                    textureDir,
                    materialConfig,
                    info, common, pages,
                    chars.associateBy { it.id },
                    kernigs.groupBy { it.first }
                            .map { groupByBMFonts ->
                                Pair(groupByBMFonts.key, groupByBMFonts.value.associate { Pair(it.first, it.amount) })
                            }.toMap())
        }

        private fun parseChunk(chunk: String): Pair<String, String>? {
            val items = chunk.splitWithEscaping('=')
            return if (items.size == 2) {
                Pair(items[0], items[1])
            } else {
                null
            }
        }

        private fun convertChunk(chunks: List<String>): Chunk? {
            val cmd = chunks.firstOrNull() ?: return null
            val values = chunks.subList(1, chunks.size)
                    .mapNotNull { parseChunk(it) }
                    .toMap()
            val int = { key: String ->
                values.getOrElse(key) { null }?.toInt() ?: throw ParseError("$key is not found")
            }
            val str = { key: String ->
                val str = values.getOrElse(key) { null }
                        ?: throw ParseError("$key is not found")
                if (str.length < 2) {
                    throw ParseError("$key is not found")
                }
                str.substring(1, str.lastIndex)
            }

            return try {
                when (cmd) {
                    ChunkType.INFO.str -> Info(
                            ChunkType.INFO,
                            str("face"),
                            int("size"),
                            int("bold"),
                            int("italic"),
                            str("charset"),
                            int("bold"),
                            int("stretchH"),
                            int("smooth"),
                            int("aa"),
                            int("outline"))
                    ChunkType.COMMON.str -> Common(
                            ChunkType.COMMON,
                            int("lineHeight"),
                            int("base"),
                            Point(int("scaleW"), int("scaleH")),
                            int("pages"),
                            int("packed"),
                            int("alphaChnl"),
                            int("redChnl"),
                            int("greenChnl"),
                            int("blueChnl"))
                    ChunkType.PAGE.str -> Page(
                            ChunkType.PAGE,
                            int("id"),
                            str("file"))
                    ChunkType.CHARS.str -> Chars(
                            ChunkType.CHARS,
                            int("count"))
                    ChunkType.CHAR.str -> Char(
                            ChunkType.CHAR,
                            int("id"),
                            PointRect(int("x"), int("y"), int("width"), int("height")),
                            Point(int("xoffset"), int("yoffset")),
                            int("xadvance"),
                            int("page"),
                            int("chnl"))
                    ChunkType.KERNINGS.str -> Kernings(
                            ChunkType.KERNINGS,
                            int("count"))
                    ChunkType.KERNING.str -> Kerning(
                            ChunkType.KERNING,
                            int("first"),
                            int("second"),
                            int("amount"))
                    else -> null
                }
            } catch (e: ParseError) {
                logger.debug(e)
                null
            }
        }

        @Suppress("UNCHECKED_CAST")
        private fun <T> cast(values: List<*>?): List<T>? {
            return values?.map { it as T }
        }
    }

    enum class ChunkType(val str: String) {
        INFO("info"),
        COMMON("common"),
        PAGE("page"),
        CHARS("chars"),
        CHAR("char"),
        KERNINGS("kernings"),
        KERNING("kerning"),
    }

    class ParseError(e: String) : Error(e)

    class InvalidFormat(e: String) : Error(e)

    abstract class Chunk(val type: ChunkType)

    class Info(
            type: ChunkType,
            val face: String,
            val size: Int,
            val bold: Int,
            val italic: Int,
            val charset: String,
            val unicode: Int,
            val stretchH: Int,
            val smooth: Int,
            val aa: Int,
            val outline: Int) : Chunk(type)

    class Common(
            type: ChunkType,
            val lineHeight: Int,
            val base: Int,
            val scale: Point,
            val page: Int,
            val packed: Int,
            val alphaChnl: Int,
            val redChnl: Int,
            val greenChnl: Int,
            val blueChnl: Int) : Chunk(type)

    class Page(
            type: ChunkType,
            val id: Int,
            val file: String) : Chunk(type)

    class Chars(type: ChunkType,
                val count: Int) : Chunk(type)

    class Char(
            type: ChunkType,
            val id: Int,
            val rect: PointRect,
            val offset: Point,
            val xAdvance: Int,
            val page: Int,
            val chnl: Int) : Chunk(type)

    class Kernings(
            type: ChunkType,
            val count: Int) : Chunk(type)

    class Kerning(
            type: ChunkType,
            val first: Int,
            val second: Int,
            val amount: Int) : Chunk(type)

    val materials = pages.map {
        val texture = Texture.load(Path.resolve(textureDir, it.file))
        it.id to Material(materialConfig, listOf(texture ?: Texture.emptyTexture()))
    }.toMap()

    override fun dispose() {
        materials.forEach { it.value.dispose() }
    }
}
