package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkSpecializationInfo(
    val mapEntities: List<VkSpecializationMapEntry>,
    val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as VkSpecializationInfo

        if (mapEntities != other.mapEntities) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mapEntities.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}

data class VkSpecializationMapEntry(
    val constantID: Int,
    val offset: Int,
    val size: Long
)
