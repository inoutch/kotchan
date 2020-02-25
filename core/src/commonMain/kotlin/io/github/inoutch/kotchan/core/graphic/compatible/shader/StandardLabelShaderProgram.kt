package io.github.inoutch.kotchan.core.graphic.compatible.shader

import io.github.inoutch.kotchan.core.KotchanGlobalContext
import io.github.inoutch.kotchan.core.graphic.compatible.Texture
import io.github.inoutch.kotchan.core.graphic.compatible.shader.attribute.Attribute
import io.github.inoutch.kotchan.core.graphic.compatible.shader.attribute.AttributeType
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.DescriptorSet
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformMatrix4F
import io.github.inoutch.kotchan.core.graphic.compatible.shader.descriptor.UniformTextureArray
import io.github.inoutch.kotchan.extension.toByteArray
import io.github.inoutch.kotchan.math.Matrix4F

val standardLabelVertCode = intArrayOf(
        0x03, 0x02, 0x23, 0x07, 0x00, 0x00, 0x01, 0x00, 0x08, 0x00, 0x08, 0x00, 0x32, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x11, 0x00, 0x02, 0x00, 0x01, 0x00, 0x00, 0x00, 0x0b, 0x00, 0x06, 0x00,
        0x01, 0x00, 0x00, 0x00, 0x47, 0x4c, 0x53, 0x4c, 0x2e, 0x73, 0x74, 0x64, 0x2e, 0x34, 0x35, 0x30,
        0x00, 0x00, 0x00, 0x00, 0x0e, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00,
        0x0f, 0x00, 0x0d, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x6d, 0x61, 0x69, 0x6e,
        0x00, 0x00, 0x00, 0x00, 0x09, 0x00, 0x00, 0x00, 0x0b, 0x00, 0x00, 0x00, 0x0f, 0x00, 0x00, 0x00,
        0x11, 0x00, 0x00, 0x00, 0x14, 0x00, 0x00, 0x00, 0x16, 0x00, 0x00, 0x00, 0x1d, 0x00, 0x00, 0x00,
        0x29, 0x00, 0x00, 0x00, 0x03, 0x00, 0x03, 0x00, 0x02, 0x00, 0x00, 0x00, 0xc2, 0x01, 0x00, 0x00,
        0x05, 0x00, 0x04, 0x00, 0x04, 0x00, 0x00, 0x00, 0x6d, 0x61, 0x69, 0x6e, 0x00, 0x00, 0x00, 0x00,
        0x05, 0x00, 0x05, 0x00, 0x09, 0x00, 0x00, 0x00, 0x6f, 0x75, 0x74, 0x43, 0x6f, 0x6c, 0x6f, 0x72,
        0x00, 0x00, 0x00, 0x00, 0x05, 0x00, 0x04, 0x00, 0x0b, 0x00, 0x00, 0x00, 0x63, 0x6f, 0x6c, 0x6f,
        0x72, 0x00, 0x00, 0x00, 0x05, 0x00, 0x05, 0x00, 0x0f, 0x00, 0x00, 0x00, 0x6f, 0x75, 0x74, 0x54,
        0x65, 0x78, 0x63, 0x6f, 0x6f, 0x72, 0x64, 0x00, 0x05, 0x00, 0x05, 0x00, 0x11, 0x00, 0x00, 0x00,
        0x74, 0x65, 0x78, 0x63, 0x6f, 0x6f, 0x72, 0x64, 0x00, 0x00, 0x00, 0x00, 0x05, 0x00, 0x06, 0x00,
        0x14, 0x00, 0x00, 0x00, 0x6f, 0x75, 0x74, 0x54, 0x65, 0x78, 0x4e, 0x75, 0x6d, 0x62, 0x65, 0x72,
        0x00, 0x00, 0x00, 0x00, 0x05, 0x00, 0x05, 0x00, 0x16, 0x00, 0x00, 0x00, 0x74, 0x65, 0x78, 0x4e,
        0x75, 0x6d, 0x62, 0x65, 0x72, 0x00, 0x00, 0x00, 0x05, 0x00, 0x06, 0x00, 0x1b, 0x00, 0x00, 0x00,
        0x67, 0x6c, 0x5f, 0x50, 0x65, 0x72, 0x56, 0x65, 0x72, 0x74, 0x65, 0x78, 0x00, 0x00, 0x00, 0x00,
        0x06, 0x00, 0x06, 0x00, 0x1b, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x67, 0x6c, 0x5f, 0x50,
        0x6f, 0x73, 0x69, 0x74, 0x69, 0x6f, 0x6e, 0x00, 0x06, 0x00, 0x07, 0x00, 0x1b, 0x00, 0x00, 0x00,
        0x01, 0x00, 0x00, 0x00, 0x67, 0x6c, 0x5f, 0x50, 0x6f, 0x69, 0x6e, 0x74, 0x53, 0x69, 0x7a, 0x65,
        0x00, 0x00, 0x00, 0x00, 0x06, 0x00, 0x07, 0x00, 0x1b, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00,
        0x67, 0x6c, 0x5f, 0x43, 0x6c, 0x69, 0x70, 0x44, 0x69, 0x73, 0x74, 0x61, 0x6e, 0x63, 0x65, 0x00,
        0x06, 0x00, 0x07, 0x00, 0x1b, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x67, 0x6c, 0x5f, 0x43,
        0x75, 0x6c, 0x6c, 0x44, 0x69, 0x73, 0x74, 0x61, 0x6e, 0x63, 0x65, 0x00, 0x05, 0x00, 0x03, 0x00,
        0x1d, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x05, 0x00, 0x04, 0x00, 0x21, 0x00, 0x00, 0x00,
        0x4d, 0x61, 0x74, 0x72, 0x69, 0x78, 0x00, 0x00, 0x06, 0x00, 0x09, 0x00, 0x21, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x76, 0x69, 0x65, 0x77, 0x50, 0x72, 0x6f, 0x6a, 0x65, 0x63, 0x74, 0x69,
        0x6f, 0x6e, 0x4d, 0x61, 0x74, 0x72, 0x69, 0x78, 0x00, 0x00, 0x00, 0x00, 0x05, 0x00, 0x03, 0x00,
        0x23, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x05, 0x00, 0x05, 0x00, 0x29, 0x00, 0x00, 0x00,
        0x70, 0x6f, 0x73, 0x69, 0x74, 0x69, 0x6f, 0x6e, 0x00, 0x00, 0x00, 0x00, 0x47, 0x00, 0x04, 0x00,
        0x09, 0x00, 0x00, 0x00, 0x1e, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x47, 0x00, 0x04, 0x00,
        0x0b, 0x00, 0x00, 0x00, 0x1e, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x47, 0x00, 0x04, 0x00,
        0x0f, 0x00, 0x00, 0x00, 0x1e, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x47, 0x00, 0x04, 0x00,
        0x11, 0x00, 0x00, 0x00, 0x1e, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x47, 0x00, 0x04, 0x00,
        0x14, 0x00, 0x00, 0x00, 0x1e, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x47, 0x00, 0x04, 0x00,
        0x16, 0x00, 0x00, 0x00, 0x1e, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x48, 0x00, 0x05, 0x00,
        0x1b, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0b, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x48, 0x00, 0x05, 0x00, 0x1b, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x0b, 0x00, 0x00, 0x00,
        0x01, 0x00, 0x00, 0x00, 0x48, 0x00, 0x05, 0x00, 0x1b, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00,
        0x0b, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x48, 0x00, 0x05, 0x00, 0x1b, 0x00, 0x00, 0x00,
        0x03, 0x00, 0x00, 0x00, 0x0b, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x47, 0x00, 0x03, 0x00,
        0x1b, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x48, 0x00, 0x04, 0x00, 0x21, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x05, 0x00, 0x00, 0x00, 0x48, 0x00, 0x05, 0x00, 0x21, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x23, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x48, 0x00, 0x05, 0x00,
        0x21, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x07, 0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00,
        0x47, 0x00, 0x03, 0x00, 0x21, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x47, 0x00, 0x04, 0x00,
        0x23, 0x00, 0x00, 0x00, 0x22, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x47, 0x00, 0x04, 0x00,
        0x23, 0x00, 0x00, 0x00, 0x21, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x47, 0x00, 0x04, 0x00,
        0x29, 0x00, 0x00, 0x00, 0x1e, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x13, 0x00, 0x02, 0x00,
        0x02, 0x00, 0x00, 0x00, 0x21, 0x00, 0x03, 0x00, 0x03, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00,
        0x16, 0x00, 0x03, 0x00, 0x06, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x17, 0x00, 0x04, 0x00,
        0x07, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x20, 0x00, 0x04, 0x00,
        0x08, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x07, 0x00, 0x00, 0x00, 0x3b, 0x00, 0x04, 0x00,
        0x08, 0x00, 0x00, 0x00, 0x09, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x20, 0x00, 0x04, 0x00,
        0x0a, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x07, 0x00, 0x00, 0x00, 0x3b, 0x00, 0x04, 0x00,
        0x0a, 0x00, 0x00, 0x00, 0x0b, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x17, 0x00, 0x04, 0x00,
        0x0d, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x20, 0x00, 0x04, 0x00,
        0x0e, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x0d, 0x00, 0x00, 0x00, 0x3b, 0x00, 0x04, 0x00,
        0x0e, 0x00, 0x00, 0x00, 0x0f, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x20, 0x00, 0x04, 0x00,
        0x10, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x0d, 0x00, 0x00, 0x00, 0x3b, 0x00, 0x04, 0x00,
        0x10, 0x00, 0x00, 0x00, 0x11, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x20, 0x00, 0x04, 0x00,
        0x13, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x3b, 0x00, 0x04, 0x00,
        0x13, 0x00, 0x00, 0x00, 0x14, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x20, 0x00, 0x04, 0x00,
        0x15, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x3b, 0x00, 0x04, 0x00,
        0x15, 0x00, 0x00, 0x00, 0x16, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x15, 0x00, 0x04, 0x00,
        0x18, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x2b, 0x00, 0x04, 0x00,
        0x18, 0x00, 0x00, 0x00, 0x19, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x1c, 0x00, 0x04, 0x00,
        0x1a, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x19, 0x00, 0x00, 0x00, 0x1e, 0x00, 0x06, 0x00,
        0x1b, 0x00, 0x00, 0x00, 0x07, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x1a, 0x00, 0x00, 0x00,
        0x1a, 0x00, 0x00, 0x00, 0x20, 0x00, 0x04, 0x00, 0x1c, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00,
        0x1b, 0x00, 0x00, 0x00, 0x3b, 0x00, 0x04, 0x00, 0x1c, 0x00, 0x00, 0x00, 0x1d, 0x00, 0x00, 0x00,
        0x03, 0x00, 0x00, 0x00, 0x15, 0x00, 0x04, 0x00, 0x1e, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00,
        0x01, 0x00, 0x00, 0x00, 0x2b, 0x00, 0x04, 0x00, 0x1e, 0x00, 0x00, 0x00, 0x1f, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x18, 0x00, 0x04, 0x00, 0x20, 0x00, 0x00, 0x00, 0x07, 0x00, 0x00, 0x00,
        0x04, 0x00, 0x00, 0x00, 0x1e, 0x00, 0x03, 0x00, 0x21, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00,
        0x20, 0x00, 0x04, 0x00, 0x22, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x21, 0x00, 0x00, 0x00,
        0x3b, 0x00, 0x04, 0x00, 0x22, 0x00, 0x00, 0x00, 0x23, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00,
        0x20, 0x00, 0x04, 0x00, 0x24, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00,
        0x17, 0x00, 0x04, 0x00, 0x27, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00,
        0x20, 0x00, 0x04, 0x00, 0x28, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x27, 0x00, 0x00, 0x00,
        0x3b, 0x00, 0x04, 0x00, 0x28, 0x00, 0x00, 0x00, 0x29, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00,
        0x2b, 0x00, 0x04, 0x00, 0x06, 0x00, 0x00, 0x00, 0x2b, 0x00, 0x00, 0x00, 0x00, 0x00, 0x80, 0x3f,
        0x36, 0x00, 0x05, 0x00, 0x02, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x03, 0x00, 0x00, 0x00, 0xf8, 0x00, 0x02, 0x00, 0x05, 0x00, 0x00, 0x00, 0x3d, 0x00, 0x04, 0x00,
        0x07, 0x00, 0x00, 0x00, 0x0c, 0x00, 0x00, 0x00, 0x0b, 0x00, 0x00, 0x00, 0x3e, 0x00, 0x03, 0x00,
        0x09, 0x00, 0x00, 0x00, 0x0c, 0x00, 0x00, 0x00, 0x3d, 0x00, 0x04, 0x00, 0x0d, 0x00, 0x00, 0x00,
        0x12, 0x00, 0x00, 0x00, 0x11, 0x00, 0x00, 0x00, 0x3e, 0x00, 0x03, 0x00, 0x0f, 0x00, 0x00, 0x00,
        0x12, 0x00, 0x00, 0x00, 0x3d, 0x00, 0x04, 0x00, 0x06, 0x00, 0x00, 0x00, 0x17, 0x00, 0x00, 0x00,
        0x16, 0x00, 0x00, 0x00, 0x3e, 0x00, 0x03, 0x00, 0x14, 0x00, 0x00, 0x00, 0x17, 0x00, 0x00, 0x00,
        0x41, 0x00, 0x05, 0x00, 0x24, 0x00, 0x00, 0x00, 0x25, 0x00, 0x00, 0x00, 0x23, 0x00, 0x00, 0x00,
        0x1f, 0x00, 0x00, 0x00, 0x3d, 0x00, 0x04, 0x00, 0x20, 0x00, 0x00, 0x00, 0x26, 0x00, 0x00, 0x00,
        0x25, 0x00, 0x00, 0x00, 0x3d, 0x00, 0x04, 0x00, 0x27, 0x00, 0x00, 0x00, 0x2a, 0x00, 0x00, 0x00,
        0x29, 0x00, 0x00, 0x00, 0x51, 0x00, 0x05, 0x00, 0x06, 0x00, 0x00, 0x00, 0x2c, 0x00, 0x00, 0x00,
        0x2a, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x51, 0x00, 0x05, 0x00, 0x06, 0x00, 0x00, 0x00,
        0x2d, 0x00, 0x00, 0x00, 0x2a, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x51, 0x00, 0x05, 0x00,
        0x06, 0x00, 0x00, 0x00, 0x2e, 0x00, 0x00, 0x00, 0x2a, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00,
        0x50, 0x00, 0x07, 0x00, 0x07, 0x00, 0x00, 0x00, 0x2f, 0x00, 0x00, 0x00, 0x2c, 0x00, 0x00, 0x00,
        0x2d, 0x00, 0x00, 0x00, 0x2e, 0x00, 0x00, 0x00, 0x2b, 0x00, 0x00, 0x00, 0x91, 0x00, 0x05, 0x00,
        0x07, 0x00, 0x00, 0x00, 0x30, 0x00, 0x00, 0x00, 0x26, 0x00, 0x00, 0x00, 0x2f, 0x00, 0x00, 0x00,
        0x41, 0x00, 0x05, 0x00, 0x08, 0x00, 0x00, 0x00, 0x31, 0x00, 0x00, 0x00, 0x1d, 0x00, 0x00, 0x00,
        0x1f, 0x00, 0x00, 0x00, 0x3e, 0x00, 0x03, 0x00, 0x31, 0x00, 0x00, 0x00, 0x30, 0x00, 0x00, 0x00,
        0xfd, 0x00, 0x01, 0x00, 0x38, 0x00, 0x01, 0x00
).toByteArray()

val standardLabelFragCode = intArrayOf(
        0x03, 0x02, 0x23, 0x07, 0x00, 0x00, 0x01, 0x00, 0x08, 0x00, 0x08, 0x00, 0x22, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x11, 0x00, 0x02, 0x00, 0x01, 0x00, 0x00, 0x00, 0x0b, 0x00, 0x06, 0x00,
        0x01, 0x00, 0x00, 0x00, 0x47, 0x4c, 0x53, 0x4c, 0x2e, 0x73, 0x74, 0x64, 0x2e, 0x34, 0x35, 0x30,
        0x00, 0x00, 0x00, 0x00, 0x0e, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00,
        0x0f, 0x00, 0x09, 0x00, 0x04, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x6d, 0x61, 0x69, 0x6e,
        0x00, 0x00, 0x00, 0x00, 0x09, 0x00, 0x00, 0x00, 0x12, 0x00, 0x00, 0x00, 0x1b, 0x00, 0x00, 0x00,
        0x1f, 0x00, 0x00, 0x00, 0x10, 0x00, 0x03, 0x00, 0x04, 0x00, 0x00, 0x00, 0x07, 0x00, 0x00, 0x00,
        0x03, 0x00, 0x03, 0x00, 0x02, 0x00, 0x00, 0x00, 0xc2, 0x01, 0x00, 0x00, 0x05, 0x00, 0x04, 0x00,
        0x04, 0x00, 0x00, 0x00, 0x6d, 0x61, 0x69, 0x6e, 0x00, 0x00, 0x00, 0x00, 0x05, 0x00, 0x05, 0x00,
        0x09, 0x00, 0x00, 0x00, 0x6f, 0x75, 0x74, 0x43, 0x6f, 0x6c, 0x6f, 0x72, 0x00, 0x00, 0x00, 0x00,
        0x05, 0x00, 0x05, 0x00, 0x10, 0x00, 0x00, 0x00, 0x74, 0x65, 0x78, 0x53, 0x61, 0x6d, 0x70, 0x6c,
        0x65, 0x72, 0x73, 0x00, 0x05, 0x00, 0x05, 0x00, 0x12, 0x00, 0x00, 0x00, 0x74, 0x65, 0x78, 0x4e,
        0x75, 0x6d, 0x62, 0x65, 0x72, 0x00, 0x00, 0x00, 0x05, 0x00, 0x05, 0x00, 0x1b, 0x00, 0x00, 0x00,
        0x74, 0x65, 0x78, 0x63, 0x6f, 0x6f, 0x72, 0x64, 0x00, 0x00, 0x00, 0x00, 0x05, 0x00, 0x04, 0x00,
        0x1f, 0x00, 0x00, 0x00, 0x63, 0x6f, 0x6c, 0x6f, 0x72, 0x00, 0x00, 0x00, 0x47, 0x00, 0x04, 0x00,
        0x09, 0x00, 0x00, 0x00, 0x1e, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x47, 0x00, 0x04, 0x00,
        0x10, 0x00, 0x00, 0x00, 0x22, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x47, 0x00, 0x04, 0x00,
        0x10, 0x00, 0x00, 0x00, 0x21, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x47, 0x00, 0x04, 0x00,
        0x12, 0x00, 0x00, 0x00, 0x1e, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x47, 0x00, 0x04, 0x00,
        0x1b, 0x00, 0x00, 0x00, 0x1e, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x47, 0x00, 0x04, 0x00,
        0x1f, 0x00, 0x00, 0x00, 0x1e, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x13, 0x00, 0x02, 0x00,
        0x02, 0x00, 0x00, 0x00, 0x21, 0x00, 0x03, 0x00, 0x03, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00,
        0x16, 0x00, 0x03, 0x00, 0x06, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x17, 0x00, 0x04, 0x00,
        0x07, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x20, 0x00, 0x04, 0x00,
        0x08, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x07, 0x00, 0x00, 0x00, 0x3b, 0x00, 0x04, 0x00,
        0x08, 0x00, 0x00, 0x00, 0x09, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x19, 0x00, 0x09, 0x00,
        0x0a, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x1b, 0x00, 0x03, 0x00, 0x0b, 0x00, 0x00, 0x00, 0x0a, 0x00, 0x00, 0x00, 0x15, 0x00, 0x04, 0x00,
        0x0c, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x2b, 0x00, 0x04, 0x00,
        0x0c, 0x00, 0x00, 0x00, 0x0d, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x1c, 0x00, 0x04, 0x00,
        0x0e, 0x00, 0x00, 0x00, 0x0b, 0x00, 0x00, 0x00, 0x0d, 0x00, 0x00, 0x00, 0x20, 0x00, 0x04, 0x00,
        0x0f, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0e, 0x00, 0x00, 0x00, 0x3b, 0x00, 0x04, 0x00,
        0x0f, 0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x20, 0x00, 0x04, 0x00,
        0x11, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x3b, 0x00, 0x04, 0x00,
        0x11, 0x00, 0x00, 0x00, 0x12, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x15, 0x00, 0x04, 0x00,
        0x14, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x20, 0x00, 0x04, 0x00,
        0x16, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0b, 0x00, 0x00, 0x00, 0x17, 0x00, 0x04, 0x00,
        0x19, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x20, 0x00, 0x04, 0x00,
        0x1a, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x19, 0x00, 0x00, 0x00, 0x3b, 0x00, 0x04, 0x00,
        0x1a, 0x00, 0x00, 0x00, 0x1b, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x20, 0x00, 0x04, 0x00,
        0x1e, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x07, 0x00, 0x00, 0x00, 0x3b, 0x00, 0x04, 0x00,
        0x1e, 0x00, 0x00, 0x00, 0x1f, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x36, 0x00, 0x05, 0x00,
        0x02, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00,
        0xf8, 0x00, 0x02, 0x00, 0x05, 0x00, 0x00, 0x00, 0x3d, 0x00, 0x04, 0x00, 0x06, 0x00, 0x00, 0x00,
        0x13, 0x00, 0x00, 0x00, 0x12, 0x00, 0x00, 0x00, 0x6e, 0x00, 0x04, 0x00, 0x14, 0x00, 0x00, 0x00,
        0x15, 0x00, 0x00, 0x00, 0x13, 0x00, 0x00, 0x00, 0x41, 0x00, 0x05, 0x00, 0x16, 0x00, 0x00, 0x00,
        0x17, 0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x15, 0x00, 0x00, 0x00, 0x3d, 0x00, 0x04, 0x00,
        0x0b, 0x00, 0x00, 0x00, 0x18, 0x00, 0x00, 0x00, 0x17, 0x00, 0x00, 0x00, 0x3d, 0x00, 0x04, 0x00,
        0x19, 0x00, 0x00, 0x00, 0x1c, 0x00, 0x00, 0x00, 0x1b, 0x00, 0x00, 0x00, 0x57, 0x00, 0x05, 0x00,
        0x07, 0x00, 0x00, 0x00, 0x1d, 0x00, 0x00, 0x00, 0x18, 0x00, 0x00, 0x00, 0x1c, 0x00, 0x00, 0x00,
        0x3d, 0x00, 0x04, 0x00, 0x07, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x1f, 0x00, 0x00, 0x00,
        0x85, 0x00, 0x05, 0x00, 0x07, 0x00, 0x00, 0x00, 0x21, 0x00, 0x00, 0x00, 0x1d, 0x00, 0x00, 0x00,
        0x20, 0x00, 0x00, 0x00, 0x3e, 0x00, 0x03, 0x00, 0x09, 0x00, 0x00, 0x00, 0x21, 0x00, 0x00, 0x00,
        0xfd, 0x00, 0x01, 0x00, 0x38, 0x00, 0x01, 0x00
).toByteArray()

val standardLabelVertText = """
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

attribute vec4 point;
attribute vec4 color;
attribute vec2 texcoord;
attribute float texNumber;

uniform mat4 uViewProjectionMatrix;

varying vec4 vColor;
varying vec2 vTexcoord;
varying float vTexNumber;

void main(void) {
    vColor = color;
    vTexcoord = texcoord;
    vTexNumber = texNumber;
    gl_Position = uViewProjectionMatrix * point;
}
"""

val standardLabelFragText = """
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

varying vec4 vColor;
varying vec2 vTexcoord;
varying float vTexNumber;

uniform sampler2D uTextures[8];

void main(void) {
    gl_FragColor = vColor * texture2D(uTextures[int(vTexNumber)], vTexcoord);
}
"""

open class StandardLabelShaderProgram(
    shaderSource: ShaderSource = createShaderSource(),
    extraDescriptorSets: List<DescriptorSet> = emptyList()
) : ShaderProgram(
        shaderSource,
        listOf(*createDescriptorSets().toTypedArray(), *extraDescriptorSets.toTypedArray()),
        listOf(
                Attribute(0, "point", 3, AttributeType.FLOAT),
                Attribute(1, "color", 4, AttributeType.FLOAT),
                Attribute(2, "texcoord", 2, AttributeType.FLOAT),
                Attribute(3, "texNumber", 1, AttributeType.FLOAT)
        )
) {
    companion object {
        const val TEXTURE_COUNT_MAX = 8

        private fun createShaderSource(): ShaderSource {
            return ShaderSource(
                    standardLabelVertText,
                    standardLabelFragText,
                    standardLabelVertCode,
                    standardLabelFragCode
            )
        }

        private fun createDescriptorSets(): List<DescriptorSet> {
            return listOf(
                    KotchanGlobalContext.graphic.createUniformMatrix4F(0, "uViewProjectionMatrix"),
                    KotchanGlobalContext.graphic.createUniformTextureArray(1, "uTextures", TEXTURE_COUNT_MAX)
            )
        }
    }

    private val viewProjectionMatrixUniform = descriptorSets[0] as UniformMatrix4F

    private val textureArrayUniform = descriptorSets[1] as UniformTextureArray

    fun prepare(mvpMatrix: Matrix4F, textures: List<Texture>) {
        viewProjectionMatrixUniform.set(mvpMatrix)
        textureArrayUniform.set(textures)
    }
}
