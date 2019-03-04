package io.github.inoutch.kotchan.utility.graphic.vulkan

import io.github.inoutch.kotchan.utility.MemScope

fun VkAttachmentReference.copyToNative(native: org.lwjgl.vulkan.VkAttachmentReference) {
    native.attachment(attachment)
            .layout(layout.value)
}

fun VkAttachmentReference.toNative(memScope: MemScope): org.lwjgl.vulkan.VkAttachmentReference =
        memScope.add(org.lwjgl.vulkan.VkAttachmentReference.calloc()
                .also { copyToNative(it) })

fun List<VkAttachmentReference>.toNative(memScope: MemScope): org.lwjgl.vulkan.VkAttachmentReference.Buffer? {
    if (this.isEmpty()) {
        return null
    }
    return memScope.add(org.lwjgl.vulkan.VkAttachmentReference.calloc(this.size)
            .also { native -> forEachIndexed { index, x -> x.copyToNative(native[index]) } })
}
