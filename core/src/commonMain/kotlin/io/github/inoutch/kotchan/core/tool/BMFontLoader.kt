package io.github.inoutch.kotchan.core.tool

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.file
import io.github.inoutch.kotchan.core.io.file.readTextAsync
import io.github.inoutch.kotchan.extension.splitWithEscaping
import io.github.inoutch.kotchan.math.RectI
import io.github.inoutch.kotchan.math.Vector2I

class BMFontLoader private constructor() {
    companion object {
        @ExperimentalStdlibApi
        suspend fun loadFromResourceWithError(filepath: String): BMFont {
            val fullPath = file.getResourcePath(filepath)
            checkNotNull(fullPath) { "Failed to get resource path of $filepath" }
            return loadWithError(fullPath)
        }

        @ExperimentalStdlibApi
        suspend fun loadWithError(filepath: String): BMFont {
            val text = file.readTextAsync(filepath).await()
            checkNotNull(text) { "Failed to read $filepath" }
            return load(text)
        }

        fun load(source: String): BMFont {
            val lines = source.replace("\r", "").split("\n")
            val chunks = lines
                    .mapNotNull { line -> convertChunk(line.splitWithEscaping(' ').filter { it.isNotEmpty() }) }
                    .groupBy { it.type }

            val infos = chunks[ChunkType.INFO]?.filterIsInstance<Info>()
            val info = checkNotNull(infos?.firstOrNull()) { "info chunk is not found" }

            val commons = chunks[ChunkType.COMMON]?.filterIsInstance<Common>()
            val common = checkNotNull(commons?.firstOrNull()) { "common chunk is not found" }

            val pages = checkNotNull(chunks[ChunkType.PAGE]?.filterIsInstance<Page>()) {
                "page chunk is not found"
            }
            val chars = checkNotNull(chunks[ChunkType.CHAR]?.filterIsInstance<Char>()) {
                "char chunk is not found"
            }

            val kernigs = chunks[ChunkType.KERNING]?.filterIsInstance<Kerning>() ?: emptyList()

            val charInfos = chunks[ChunkType.CHARS]?.filterIsInstance<Chars>()
            val charInfo = checkNotNull(charInfos?.firstOrNull()) { "chars chunk is not found" }

            val kerningInfos = chunks[ChunkType.KERNINGS]?.filterIsInstance<Kernings>()
            val kerningInfo = checkNotNull(kerningInfos?.firstOrNull()) { "kernigs chunk is not found" }

            check(charInfo.count == chars.size) { "The count of 'char' is different from 'chars'" }
            check(kerningInfo.count == kernigs.size) { "The count of 'kerning' is different from 'kernigs'" }

            return BMFont(
                    info,
                    common,
                    pages,
                    chars.associateBy { it.id },
                    KerningAmount(kernigs)
            )
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

            return when (cmd) {
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
                        int("outline")
                )
                ChunkType.COMMON.str -> Common(
                        ChunkType.COMMON,
                        int("lineHeight"),
                        int("base"),
                        Vector2I(int("scaleW"), int("scaleH")),
                        int("pages"),
                        int("packed"),
                        int("alphaChnl"),
                        int("redChnl"),
                        int("greenChnl"),
                        int("blueChnl")
                )
                ChunkType.PAGE.str -> Page(
                        ChunkType.PAGE,
                        int("id"),
                        str("file")
                )
                ChunkType.CHARS.str -> Chars(
                        ChunkType.CHARS,
                        int("count")
                )
                ChunkType.CHAR.str -> Char(
                        ChunkType.CHAR,
                        int("id"),
                        RectI(int("x"), int("y"), int("width"), int("height")),
                        Vector2I(int("xoffset"), int("yoffset")),
                        int("xadvance"),
                        int("page"),
                        int("chnl")
                )
                ChunkType.KERNINGS.str -> Kernings(
                        ChunkType.KERNINGS,
                        int("count")
                )
                ChunkType.KERNING.str -> Kerning(
                        ChunkType.KERNING,
                        int("first"),
                        int("second"),
                        int("amount")
                )
                else -> null
            }
        }
    }

    data class BMFont(
        val info: Info,
        val common: Common,
        val pages: List<Page>,
        val chars: Map<Int, Char>,
        val kernigs: KerningAmount
    ) {
        fun calcWidth(text: String): Float {
            return text.mapNotNull { chars[it.toInt()] }
                    .map { it.xAdvance.toFloat() }
                    .sum()
        }
    }

    class ParseError(e: String) : Error(e)

    enum class ChunkType(val str: String) {
        INFO("info"),
        COMMON("common"),
        PAGE("page"),
        CHARS("chars"),
        CHAR("char"),
        KERNINGS("kernings"),
        KERNING("kerning"),
    }

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
        val outline: Int
    ) : Chunk(type)

    class Common(
        type: ChunkType,
        val lineHeight: Int,
        val base: Int,
        val scale: Vector2I,
        val page: Int,
        val packed: Int,
        val alphaChnl: Int,
        val redChnl: Int,
        val greenChnl: Int,
        val blueChnl: Int
    ) : Chunk(type)

    class Page(
        type: ChunkType,
        val id: Int,
        val file: String
    ) : Chunk(type)

    class Chars(
        type: ChunkType,
        val count: Int
    ) : Chunk(type)

    class Char(
        type: ChunkType,
        val id: Int,
        val rect: RectI,
        val offset: Vector2I,
        val xAdvance: Int,
        val page: Int,
        val chnl: Int
    ) : Chunk(type)

    class Kernings(
        type: ChunkType,
        val count: Int
    ) : Chunk(type)

    class Kerning(
        type: ChunkType,
        val first: Int,
        val second: Int,
        val amount: Int
    ) : Chunk(type)

    class KerningAmount(val kernigs: List<Kerning>) {
        private val amounts: Map<Int, Map<Int, Int>> = kernigs
                .groupBy { it.first }
                .map { groupByBMFonts ->
                    Pair(
                            groupByBMFonts.key,
                            groupByBMFonts.value.associate { Pair(it.first, it.amount) })
                }.toMap()

        fun getAmount(firstChar: Int, secondChar: Int): Int {
            return amounts.getValue(firstChar).getValue(secondChar)
        }

        fun getAmountOrNull(firstChar: Int, secondChar: Int): Int? {
            return amounts[firstChar]?.get(secondChar)
        }
    }
}
