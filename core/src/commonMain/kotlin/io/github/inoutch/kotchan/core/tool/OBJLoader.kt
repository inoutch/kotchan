package io.github.inoutch.kotchan.core.tool

import io.github.inoutch.kotchan.core.graphic.Mesh
import io.github.inoutch.kotchan.extension.splitWithEscaping
import io.github.inoutch.kotchan.math.Vector2F
import io.github.inoutch.kotchan.math.Vector3F
import io.github.inoutch.kotchan.math.Vector4F

class OBJLoader private constructor() {
    companion object {
        fun loadObj(source: String): Bundle {
            val chunks = source
                    .replace("\r", "")
                    .split("\n")
                    .asSequence()
                    .filter { it.isNotBlank() }
                    .mapNotNull { line ->
                        val data = line.splitWithEscaping(' ')
                                .filter { it.isNotBlank() }
                        if (data.size < 2) {
                            return@mapNotNull null
                        }
                        parseChunk(data.first(), data)
                    }
                    .toList()
            val objects = chunks
                    .asSequence()
                    .mapIndexedNotNull { index, chunk ->
                        if (chunk is Object) index else null
                    }
                    .plus(chunks.size)
                    .fold(mutableListOf<IntRange>()) { x, y ->
                        val last = x.lastOrNull()?.last ?: 0
                        x.add(IntRange(last, y))
                        x
                    }
                    .drop(1)
                    .map {
                        val obj = chunks[it.first] as Object
                        val range = IntRange(it.first + 1, it.last - 1)
                        obj to chunks.slice(range)
                    }
                    .toMap()
            val materialLib = chunks.find { it is MaterialLib } as MaterialLib?
            return Bundle(
                    materialLib,
                    objects.map { obj ->
                        val f1 = obj.value.filterIsInstance<Face3I>()
                                .map { it.toFace3V() }.toTypedArray()
                        val f2 = obj.value.filterIsInstance<Face4I>()
                                .map { it.toFace3I() }.flatten().map { it.toFace3V() }.toTypedArray()
                        val f3 = obj.value.filterIsInstance<Face3V>()
                                .toTypedArray()
                        val f4 = obj.value.filterIsInstance<Face4V>().map { it.toFace3V() }.flatten()
                                .toTypedArray()
                        val faces: List<Face3V> = listOf(*f1, *f2, *f3, *f4)
                        ObjectBundle(
                                obj.key.name,
                                obj.value.filterIsInstance<Vertex>(),
                                obj.value.filterIsInstance<VertexTexcoord>(),
                                obj.value.filterIsInstance<VertexNormal>(),
                                faces
                        )
                    }
            )
        }

        private fun parseChunk(cmd: String, data: List<String>): Chunk? {
            return try {
                when (cmd) {
                    "mtllib" -> MaterialLib(data[1])
                    "usemtl" -> UseMaterial(data[1])
                    "o" -> Object(data[1])
                    "g" -> Group(data[1])
                    "v" -> Vertex.create(data[1], data[2], data[3])
                    "vt" -> VertexTexcoord.create(data[1], data[2])
                    "vn" -> VertexNormal.create(data[1], data[2], data[3])
                    "f" -> createFace(data)
                    else -> null
                }
            } catch (e: IndexOutOfBoundsException) {
                null
            }
        }

        private fun createFace(data: List<String>): Chunk {
            val v1 = data[1].split("/")
            if (v1.size == 1) {
                return if (data.size - 1 <= 3) {
                    createFace3I(
                            data[1],
                            data[2],
                            data[3]
                    )
                } else {
                    createFace4I(
                            data[1],
                            data[2],
                            data[3],
                            data[4]
                    )
                }
            }
            check(v1.size == 3) { "Invalid face format [$data]" }
            val v2 = data[2].split("/")
            val v3 = data[3].split("/")
            if (data.size - 1 <= 3) {
                return Face3V(
                        VertexBundle.create(v1[0], v1[1], v1[2]),
                        VertexBundle.create(v2[0], v2[1], v2[2]),
                        VertexBundle.create(v3[0], v3[1], v3[2])
                )
            }
            val v4 = data[4].split("/")
            return Face4V(
                    VertexBundle.create(v1[0], v1[1], v1[2]),
                    VertexBundle.create(v2[0], v2[1], v2[2]),
                    VertexBundle.create(v3[0], v3[1], v3[2]),
                    VertexBundle.create(v4[0], v4[1], v4[2])
            )
        }

        private fun createFace3I(v1: String, v2: String, v3: String): Face3I {
            return Face3I(
                    v1.toInt(),
                    v2.toInt(),
                    v3.toInt()
            )
        }

        private fun createFace4I(v1: String, v2: String, v3: String, v4: String): Face4I {
            return Face4I(
                    v1.toInt(),
                    v2.toInt(),
                    v3.toInt(),
                    v4.toInt()
            )
        }
    }

    interface Chunk

    class MaterialLib(val filename: String) : Chunk

    class UseMaterial(val name: String) : Chunk

    class Object(val name: String) : Chunk

    class Group(val groupName: String) : Chunk

    class Vertex(val x: Float, val y: Float, val z: Float) : Chunk {
        companion object {
            fun create(x: String, y: String, z: String): Vertex {
                return Vertex(
                        x.toFloat(),
                        y.toFloat(),
                        z.toFloat()
                )
            }
        }

        fun toVector3F(): Vector3F {
            return Vector3F(x, y, z)
        }
    }

    class VertexTexcoord(val u: Float, val v: Float) : Chunk {
        companion object {
            fun create(u: String, v: String): VertexTexcoord {
                return VertexTexcoord(
                        u.toFloat(),
                        v.toFloat()
                )
            }
        }

        fun toVector2F(): Vector2F {
            return Vector2F(u, v)
        }
    }

    class VertexNormal(val x: Float, val y: Float, val z: Float) : Chunk {
        companion object {
            fun create(x: String, y: String, z: String): VertexNormal {
                return VertexNormal(
                        x.toFloat(),
                        y.toFloat(),
                        z.toFloat()
                )
            }
        }

        fun toVector3F(): Vector3F {
            return Vector3F(x, y, z)
        }
    }

    class VertexBundle(
            val v: Int,
            val t: Int,
            val n: Int
    ) {
        companion object {
            fun create(v: String, t: String, n: String): VertexBundle {
                return VertexBundle(
                        v.toInt(),
                        if (t == "") -1 else t.toInt(),
                        n.toInt()
                )
            }
        }
    }

    class Face3I(val v1: Int, val v2: Int, val v3: Int) : Chunk {
        fun toFace3V(): Face3V {
            return Face3V(
                    VertexBundle(v1, -1, -1),
                    VertexBundle(v2, -1, -1),
                    VertexBundle(v3, -1, -1)
            )
        }
    }

    class Face4I(val v1: Int, val v2: Int, val v3: Int, val v4: Int) : Chunk {
        fun toFace3I(): List<Face3I> {
            return listOf(
                    Face3I(v1, v2, v3),
                    Face3I(v1, v3, v4)
            )
        }
    }

    class Face3V(
            val v1: VertexBundle,
            val v2: VertexBundle,
            val v3: VertexBundle
    ) : Chunk

    class Face4V(
            val v1: VertexBundle,
            val v2: VertexBundle,
            val v3: VertexBundle,
            val v4: VertexBundle
    ) : Chunk {
        fun toFace3V(): List<Face3V> {
            return listOf(
                    Face3V(v1, v2, v3),
                    Face3V(v1, v3, v4)
            )
        }
    }

    data class Bundle(
            val materialLib: MaterialLib?,
            val objects: List<ObjectBundle>
    )

    data class ObjectBundle(
            val name: String,
            val vertices: List<Vertex>,
            val texcoords: List<VertexTexcoord>,
            val normals: List<VertexNormal>,
            val faces: List<Face3V>
    ) {
        fun toMesh(): Mesh {
            return Mesh(
                    faces.map {
                        listOf(
                                vertices.getOrNull(it.v1.v - 1)?.toVector3F() ?: Vector3F.Zero,
                                vertices.getOrNull(it.v2.v - 1)?.toVector3F() ?: Vector3F.Zero,
                                vertices.getOrNull(it.v3.v - 1)?.toVector3F() ?: Vector3F.Zero
                        )
                    }.flatten(),
                    faces.map {
                        listOf(
                                texcoords.getOrNull(it.v1.t - 1)?.toVector2F() ?: Vector2F.Zero,
                                texcoords.getOrNull(it.v2.t - 1)?.toVector2F() ?: Vector2F.Zero,
                                texcoords.getOrNull(it.v3.t - 1)?.toVector2F() ?: Vector2F.Zero
                        )
                    }.flatten(),
                    faces.map {
                        listOf(
                                Vector4F(1.0f),
                                Vector4F(1.0f),
                                Vector4F(1.0f)
                        )
                    }.flatten(),
                    faces.map {
                        listOf(
                                normals.getOrNull(it.v1.n - 1)?.toVector3F() ?: Vector3F.Zero,
                                normals.getOrNull(it.v2.n - 1)?.toVector3F() ?: Vector3F.Zero,
                                normals.getOrNull(it.v3.n - 1)?.toVector3F() ?: Vector3F.Zero
                        )
                    }.flatten()
            )
        }
    }
}
