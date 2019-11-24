package io.github.inoutch.kotchan.utility.graphic.vulkan

data class VkShaderModuleCreateInfo(
    val flags: Int,
    val code: ByteArray
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as VkShaderModuleCreateInfo

        if (flags != other.flags) return false
        if (!code.contentEquals(other.code)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = flags
        result = 31 * result + code.contentHashCode()
        return result
    }
}
