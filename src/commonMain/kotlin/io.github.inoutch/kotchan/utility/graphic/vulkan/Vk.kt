package io.github.inoutch.kotchan.utility.graphic.vulkan

const val FLOAT_SIZE = 4L
const val INT_SIZE = 4L

enum class VkFormat(val value: Int) {
    VK_FORMAT_UNDEFINED(0),
    VK_FORMAT_R4G4_UNORM_PACK8(1),
    VK_FORMAT_R4G4B4A4_UNORM_PACK16(2),
    VK_FORMAT_B4G4R4A4_UNORM_PACK16(3),
    VK_FORMAT_R5G6B5_UNORM_PACK16(4),
    VK_FORMAT_B5G6R5_UNORM_PACK16(5),
    VK_FORMAT_R5G5B5A1_UNORM_PACK16(6),
    VK_FORMAT_B5G5R5A1_UNORM_PACK16(7),
    VK_FORMAT_A1R5G5B5_UNORM_PACK16(8),
    VK_FORMAT_R8_UNORM(9),
    VK_FORMAT_R8_SNORM(10),
    VK_FORMAT_R8_USCALED(11),
    VK_FORMAT_R8_SSCALED(12),
    VK_FORMAT_R8_UINT(13),
    VK_FORMAT_R8_SINT(14),
    VK_FORMAT_R8_SRGB(15),
    VK_FORMAT_R8G8_UNORM(16),
    VK_FORMAT_R8G8_SNORM(17),
    VK_FORMAT_R8G8_USCALED(18),
    VK_FORMAT_R8G8_SSCALED(19),
    VK_FORMAT_R8G8_UINT(20),
    VK_FORMAT_R8G8_SINT(21),
    VK_FORMAT_R8G8_SRGB(22),
    VK_FORMAT_R8G8B8_UNORM(23),
    VK_FORMAT_R8G8B8_SNORM(24),
    VK_FORMAT_R8G8B8_USCALED(25),
    VK_FORMAT_R8G8B8_SSCALED(26),
    VK_FORMAT_R8G8B8_UINT(27),
    VK_FORMAT_R8G8B8_SINT(28),
    VK_FORMAT_R8G8B8_SRGB(29),
    VK_FORMAT_B8G8R8_UNORM(30),
    VK_FORMAT_B8G8R8_SNORM(31),
    VK_FORMAT_B8G8R8_USCALED(32),
    VK_FORMAT_B8G8R8_SSCALED(33),
    VK_FORMAT_B8G8R8_UINT(34),
    VK_FORMAT_B8G8R8_SINT(35),
    VK_FORMAT_B8G8R8_SRGB(36),
    VK_FORMAT_R8G8B8A8_UNORM(37),
    VK_FORMAT_R8G8B8A8_SNORM(38),
    VK_FORMAT_R8G8B8A8_USCALED(39),
    VK_FORMAT_R8G8B8A8_SSCALED(40),
    VK_FORMAT_R8G8B8A8_UINT(41),
    VK_FORMAT_R8G8B8A8_SINT(42),
    VK_FORMAT_R8G8B8A8_SRGB(43),
    VK_FORMAT_B8G8R8A8_UNORM(44),
    VK_FORMAT_B8G8R8A8_SNORM(45),
    VK_FORMAT_B8G8R8A8_USCALED(46),
    VK_FORMAT_B8G8R8A8_SSCALED(47),
    VK_FORMAT_B8G8R8A8_UINT(48),
    VK_FORMAT_B8G8R8A8_SINT(49),
    VK_FORMAT_B8G8R8A8_SRGB(50),
    VK_FORMAT_A8B8G8R8_UNORM_PACK32(51),
    VK_FORMAT_A8B8G8R8_SNORM_PACK32(52),
    VK_FORMAT_A8B8G8R8_USCALED_PACK32(53),
    VK_FORMAT_A8B8G8R8_SSCALED_PACK32(54),
    VK_FORMAT_A8B8G8R8_UINT_PACK32(55),
    VK_FORMAT_A8B8G8R8_SINT_PACK32(56),
    VK_FORMAT_A8B8G8R8_SRGB_PACK32(57),
    VK_FORMAT_A2R10G10B10_UNORM_PACK32(58),
    VK_FORMAT_A2R10G10B10_SNORM_PACK32(59),
    VK_FORMAT_A2R10G10B10_USCALED_PACK32(60),
    VK_FORMAT_A2R10G10B10_SSCALED_PACK32(61),
    VK_FORMAT_A2R10G10B10_UINT_PACK32(62),
    VK_FORMAT_A2R10G10B10_SINT_PACK32(63),
    VK_FORMAT_A2B10G10R10_UNORM_PACK32(64),
    VK_FORMAT_A2B10G10R10_SNORM_PACK32(65),
    VK_FORMAT_A2B10G10R10_USCALED_PACK32(66),
    VK_FORMAT_A2B10G10R10_SSCALED_PACK32(67),
    VK_FORMAT_A2B10G10R10_UINT_PACK32(68),
    VK_FORMAT_A2B10G10R10_SINT_PACK32(69),
    VK_FORMAT_R16_UNORM(70),
    VK_FORMAT_R16_SNORM(71),
    VK_FORMAT_R16_USCALED(72),
    VK_FORMAT_R16_SSCALED(73),
    VK_FORMAT_R16_UINT(74),
    VK_FORMAT_R16_SINT(75),
    VK_FORMAT_R16_SFLOAT(76),
    VK_FORMAT_R16G16_UNORM(77),
    VK_FORMAT_R16G16_SNORM(78),
    VK_FORMAT_R16G16_USCALED(79),
    VK_FORMAT_R16G16_SSCALED(80),
    VK_FORMAT_R16G16_UINT(81),
    VK_FORMAT_R16G16_SINT(82),
    VK_FORMAT_R16G16_SFLOAT(83),
    VK_FORMAT_R16G16B16_UNORM(84),
    VK_FORMAT_R16G16B16_SNORM(85),
    VK_FORMAT_R16G16B16_USCALED(86),
    VK_FORMAT_R16G16B16_SSCALED(87),
    VK_FORMAT_R16G16B16_UINT(88),
    VK_FORMAT_R16G16B16_SINT(89),
    VK_FORMAT_R16G16B16_SFLOAT(90),
    VK_FORMAT_R16G16B16A16_UNORM(91),
    VK_FORMAT_R16G16B16A16_SNORM(92),
    VK_FORMAT_R16G16B16A16_USCALED(93),
    VK_FORMAT_R16G16B16A16_SSCALED(94),
    VK_FORMAT_R16G16B16A16_UINT(95),
    VK_FORMAT_R16G16B16A16_SINT(96),
    VK_FORMAT_R16G16B16A16_SFLOAT(97),
    VK_FORMAT_R32_UINT(98),
    VK_FORMAT_R32_SINT(99),
    VK_FORMAT_R32_SFLOAT(100), // float
    VK_FORMAT_R32G32_UINT(101),
    VK_FORMAT_R32G32_SINT(102),
    VK_FORMAT_R32G32_SFLOAT(103), // vec2
    VK_FORMAT_R32G32B32_UINT(104),
    VK_FORMAT_R32G32B32_SINT(105),
    VK_FORMAT_R32G32B32_SFLOAT(106), // vec3
    VK_FORMAT_R32G32B32A32_UINT(107),
    VK_FORMAT_R32G32B32A32_SINT(108),
    VK_FORMAT_R32G32B32A32_SFLOAT(109), // vec4
    VK_FORMAT_R64_UINT(110),
    VK_FORMAT_R64_SINT(111),
    VK_FORMAT_R64_SFLOAT(112),
    VK_FORMAT_R64G64_UINT(113),
    VK_FORMAT_R64G64_SINT(114),
    VK_FORMAT_R64G64_SFLOAT(115),
    VK_FORMAT_R64G64B64_UINT(116),
    VK_FORMAT_R64G64B64_SINT(117),
    VK_FORMAT_R64G64B64_SFLOAT(118),
    VK_FORMAT_R64G64B64A64_UINT(119),
    VK_FORMAT_R64G64B64A64_SINT(120),
    VK_FORMAT_R64G64B64A64_SFLOAT(121),
    VK_FORMAT_B10G11R11_UFLOAT_PACK32(122),
    VK_FORMAT_E5B9G9R9_UFLOAT_PACK32(123),
    VK_FORMAT_D16_UNORM(124),
    VK_FORMAT_X8_D24_UNORM_PACK32(125),
    VK_FORMAT_D32_SFLOAT(126),
    VK_FORMAT_S8_UINT(127),
    VK_FORMAT_D16_UNORM_S8_UINT(128),
    VK_FORMAT_D24_UNORM_S8_UINT(129),
    VK_FORMAT_D32_SFLOAT_S8_UINT(130),
    VK_FORMAT_BC1_RGB_UNORM_BLOCK(131),
    VK_FORMAT_BC1_RGB_SRGB_BLOCK(132),
    VK_FORMAT_BC1_RGBA_UNORM_BLOCK(133),
    VK_FORMAT_BC1_RGBA_SRGB_BLOCK(134),
    VK_FORMAT_BC2_UNORM_BLOCK(135),
    VK_FORMAT_BC2_SRGB_BLOCK(136),
    VK_FORMAT_BC3_UNORM_BLOCK(137),
    VK_FORMAT_BC3_SRGB_BLOCK(138),
    VK_FORMAT_BC4_UNORM_BLOCK(139),
    VK_FORMAT_BC4_SNORM_BLOCK(140),
    VK_FORMAT_BC5_UNORM_BLOCK(141),
    VK_FORMAT_BC5_SNORM_BLOCK(142),
    VK_FORMAT_BC6H_UFLOAT_BLOCK(143),
    VK_FORMAT_BC6H_SFLOAT_BLOCK(144),
    VK_FORMAT_BC7_UNORM_BLOCK(145),
    VK_FORMAT_BC7_SRGB_BLOCK(146),
    VK_FORMAT_ETC2_R8G8B8_UNORM_BLOCK(147),
    VK_FORMAT_ETC2_R8G8B8_SRGB_BLOCK(148),
    VK_FORMAT_ETC2_R8G8B8A1_UNORM_BLOCK(149),
    VK_FORMAT_ETC2_R8G8B8A1_SRGB_BLOCK(150),
    VK_FORMAT_ETC2_R8G8B8A8_UNORM_BLOCK(151),
    VK_FORMAT_ETC2_R8G8B8A8_SRGB_BLOCK(152),
    VK_FORMAT_EAC_R11_UNORM_BLOCK(153),
    VK_FORMAT_EAC_R11_SNORM_BLOCK(154),
    VK_FORMAT_EAC_R11G11_UNORM_BLOCK(155),
    VK_FORMAT_EAC_R11G11_SNORM_BLOCK(156),
    VK_FORMAT_ASTC_4x4_UNORM_BLOCK(157),
    VK_FORMAT_ASTC_4x4_SRGB_BLOCK(158),
    VK_FORMAT_ASTC_5x4_UNORM_BLOCK(159),
    VK_FORMAT_ASTC_5x4_SRGB_BLOCK(160),
    VK_FORMAT_ASTC_5x5_UNORM_BLOCK(161),
    VK_FORMAT_ASTC_5x5_SRGB_BLOCK(162),
    VK_FORMAT_ASTC_6x5_UNORM_BLOCK(163),
    VK_FORMAT_ASTC_6x5_SRGB_BLOCK(164),
    VK_FORMAT_ASTC_6x6_UNORM_BLOCK(165),
    VK_FORMAT_ASTC_6x6_SRGB_BLOCK(166),
    VK_FORMAT_ASTC_8x5_UNORM_BLOCK(167),
    VK_FORMAT_ASTC_8x5_SRGB_BLOCK(168),
    VK_FORMAT_ASTC_8x6_UNORM_BLOCK(169),
    VK_FORMAT_ASTC_8x6_SRGB_BLOCK(170),
    VK_FORMAT_ASTC_8x8_UNORM_BLOCK(171),
    VK_FORMAT_ASTC_8x8_SRGB_BLOCK(172),
    VK_FORMAT_ASTC_10x5_UNORM_BLOCK(173),
    VK_FORMAT_ASTC_10x5_SRGB_BLOCK(174),
    VK_FORMAT_ASTC_10x6_UNORM_BLOCK(175),
    VK_FORMAT_ASTC_10x6_SRGB_BLOCK(176),
    VK_FORMAT_ASTC_10x8_UNORM_BLOCK(177),
    VK_FORMAT_ASTC_10x8_SRGB_BLOCK(178),
    VK_FORMAT_ASTC_10x10_UNORM_BLOCK(179),
    VK_FORMAT_ASTC_10x10_SRGB_BLOCK(180),
    VK_FORMAT_ASTC_12x10_UNORM_BLOCK(181),
    VK_FORMAT_ASTC_12x10_SRGB_BLOCK(182),
    VK_FORMAT_ASTC_12x12_UNORM_BLOCK(183),
    VK_FORMAT_ASTC_12x12_SRGB_BLOCK(184),
}

enum class VkColorSpaceKHR(val value: Int) {
    VK_COLOR_SPACE_SRGB_NONLINEAR_KHR(0),
    VK_COLOR_SPACE_DISPLAY_P3_NONLINEAR_EXT(1000104001),
    VK_COLOR_SPACE_EXTENDED_SRGB_LINEAR_EXT(1000104002),
    VK_COLOR_SPACE_DCI_P3_LINEAR_EXT(1000104003),
    VK_COLOR_SPACE_DCI_P3_NONLINEAR_EXT(1000104004),
    VK_COLOR_SPACE_BT709_LINEAR_EXT(1000104005),
    VK_COLOR_SPACE_BT709_NONLINEAR_EXT(1000104006),
    VK_COLOR_SPACE_BT2020_LINEAR_EXT(1000104007),
    VK_COLOR_SPACE_HDR10_ST2084_EXT(1000104008),
    VK_COLOR_SPACE_DOLBYVISION_EXT(1000104009),
    VK_COLOR_SPACE_HDR10_HLG_EXT(1000104010),
    VK_COLOR_SPACE_ADOBERGB_LINEAR_EXT(1000104011),
    VK_COLOR_SPACE_ADOBERGB_NONLINEAR_EXT(1000104012),
    VK_COLOR_SPACE_PASS_THROUGH_EXT(1000104013),
    VK_COLOR_SPACE_EXTENDED_SRGB_NONLINEAR_EXT(1000104014),
}

enum class VkPresentModeKHR(val value: Int) {
    VK_PRESENT_MODE_IMMEDIATE_KHR(0),
    VK_PRESENT_MODE_FIFO_KHR(1),
    VK_PRESENT_MODE_FIFO_RELAXED_KHR(2),
    VK_PRESENT_MODE_MAILBOX_KHR(3), // for triple buffering
}

enum class VkCommandBufferLevel(val level: Int) {
    VK_COMMAND_BUFFER_LEVEL_PRIMARY(0),
    VK_COMMAND_BUFFER_LEVEL_SECONDARY(1),
}

enum class VkSampleFlagBits(val value: Int) {
    VK_SAMPLE_COUNT_1_BIT(0x1),
    VK_SAMPLE_COUNT_2_BIT(0x2),
    VK_SAMPLE_COUNT_4_BIT(0x4),
    VK_SAMPLE_COUNT_8_BIT(0x8),
    VK_SAMPLE_COUNT_16_BIT(0x10),
    VK_SAMPLE_COUNT_32_BIT(0x20),
    VK_SAMPLE_COUNT_64_BIT(0x40),
}

enum class VkAttachmentLoadOp(val value: Int) {
    VK_ATTACHMENT_LOAD_OP_LOAD(0),
    VK_ATTACHMENT_LOAD_OP_CLEAR(1),
    VK_ATTACHMENT_LOAD_OP_DONT_CARE(2),
}

enum class VkAttachmentStoreOp(val value: Int) {
    VK_ATTACHMENT_STORE_OP_STORE(0),
    VK_ATTACHMENT_STORE_OP_DONT_CARE(1),
}

enum class VkImageLayout(val value: Int) {
    VK_IMAGE_LAYOUT_UNDEFINED(0),
    VK_IMAGE_LAYOUT_GENERAL(1),
    VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL(2),
    VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL(3),
    VK_IMAGE_LAYOUT_DEPTH_STENCIL_READ_ONLY_OPTIMAL(4),
    VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL(5),
    VK_IMAGE_LAYOUT_TRANSFER_SRC_OPTIMAL(6),
    VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL(7),
    VK_IMAGE_LAYOUT_PREINITIALIZED(8),
    VK_IMAGE_LAYOUT_DEPTH_READ_ONLY_STENCIL_ATTACHMENT_OPTIMAL(1000117000),
    VK_IMAGE_LAYOUT_DEPTH_ATTACHMENT_STENCIL_READ_ONLY_OPTIMAL(1000117001),
    VK_IMAGE_LAYOUT_PRESENT_SRC_KHR(1000001002),
    VK_IMAGE_LAYOUT_SHARED_PRESENT_KHR(1000111000),
    VK_IMAGE_LAYOUT_SHADING_RATE_OPTIMAL_NV(1000164003),
    VK_IMAGE_LAYOUT_FRAGMENT_DENSITY_MAP_OPTIMAL_EXT(1000218000),
}

enum class VkPipelineBindPoint(val value: Int) {
    VK_PIPELINE_BIND_POINT_GRAPHICS(0),
    VK_PIPELINE_BIND_POINT_COMPUTE(1),
}

enum class VkPipelineStageFlagBits(val value: Int) {
    VK_PIPELINE_STAGE_TOP_OF_PIPE_BIT(0x1),
    VK_PIPELINE_STAGE_DRAW_INDIRECT_BIT(0x2),
    VK_PIPELINE_STAGE_VERTEX_INPUT_BIT(0x4),
    VK_PIPELINE_STAGE_VERTEX_SHADER_BIT(0x8),
    VK_PIPELINE_STAGE_TESSELLATION_CONTROL_SHADER_BIT(0x10),
    VK_PIPELINE_STAGE_TESSELLATION_EVALUATION_SHADER_BIT(0x20),
    VK_PIPELINE_STAGE_GEOMETRY_SHADER_BIT(0x40),
    VK_PIPELINE_STAGE_FRAGMENT_SHADER_BIT(0x80),
    VK_PIPELINE_STAGE_EARLY_FRAGMENT_TESTS_BIT(0x100),
    VK_PIPELINE_STAGE_LATE_FRAGMENT_TESTS_BIT(0x200),
    VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT(0x400),
    VK_PIPELINE_STAGE_COMPUTE_SHADER_BIT(0x800),
    VK_PIPELINE_STAGE_TRANSFER_BIT(0x1000),
    VK_PIPELINE_STAGE_BOTTOM_OF_PIPE_BIT(0x2000),
    VK_PIPELINE_STAGE_HOST_BIT(0x4000),
    VK_PIPELINE_STAGE_ALL_GRAPHICS_BIT(0x8000),
    VK_PIPELINE_STAGE_ALL_COMMANDS_BIT(0x10000),
}

enum class VkPrimitiveTopology(val value: Int) {
    VK_PRIMITIVE_TOPOLOGY_POINT_LIST(0),
    VK_PRIMITIVE_TOPOLOGY_LINE_LIST(1),
    VK_PRIMITIVE_TOPOLOGY_LINE_STRIP(2),
    VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST(3),
    VK_PRIMITIVE_TOPOLOGY_TRIANGLE_STRIP(4),
    VK_PRIMITIVE_TOPOLOGY_TRIANGLE_FAN(5),
    VK_PRIMITIVE_TOPOLOGY_LINE_LIST_WITH_ADJACENCY(6),
    VK_PRIMITIVE_TOPOLOGY_LINE_STRIP_WITH_ADJACENCY(7),
    VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST_WITH_ADJACENCY(8),
    VK_PRIMITIVE_TOPOLOGY_TRIANGLE_STRIP_WITH_ADJACENCY(9),
    VK_PRIMITIVE_TOPOLOGY_PATCH_LIST(10),
}

enum class VkPolygonMode(val value: Int) {
    VK_POLYGON_MODE_FILL(0),
    VK_POLYGON_MODE_LINE(1),
    VK_POLYGON_MODE_POINT(2),
}

enum class VkCullMode(val value: Int) {
    VK_CULL_MODE_NONE(0),
    VK_CULL_MODE_FRONT_BIT(1),
    VK_CULL_MODE_BACK_BIT(2),
    VK_CULL_MODE_FRONT_AND_BACK(3),
}

enum class VkFrontFace(val value: Int) {
    VK_FRONT_FACE_COUNTER_CLOCKWISE(0),
    VK_FRONT_FACE_CLOCKWISE(1),
}

enum class VkBlendFactor(val value: Int) {
    VK_BLEND_FACTOR_ZERO(0),
    VK_BLEND_FACTOR_ONE(1),
    VK_BLEND_FACTOR_SRC_COLOR(2),
    VK_BLEND_FACTOR_ONE_MINUS_SRC_COLOR(3),
    VK_BLEND_FACTOR_DST_COLOR(4),
    VK_BLEND_FACTOR_ONE_MINUS_DST_COLOR(5),
    VK_BLEND_FACTOR_SRC_ALPHA(6),
    VK_BLEND_FACTOR_ONE_MINUS_SRC_ALPHA(7),
    VK_BLEND_FACTOR_DST_ALPHA(8),
    VK_BLEND_FACTOR_ONE_MINUS_DST_ALPHA(9),
    VK_BLEND_FACTOR_CONSTANT_COLOR(10),
    VK_BLEND_FACTOR_ONE_MINUS_CONSTANT_COLOR(11),
    VK_BLEND_FACTOR_CONSTANT_ALPHA(12),
    VK_BLEND_FACTOR_ONE_MINUS_CONSTANT_ALPHA(13),
    VK_BLEND_FACTOR_SRC_ALPHA_SATURATE(14),
    VK_BLEND_FACTOR_SRC1_COLOR(15),
    VK_BLEND_FACTOR_ONE_MINUS_SRC1_COLOR(16),
    VK_BLEND_FACTOR_SRC1_ALPHA(17),
    VK_BLEND_FACTOR_ONE_MINUS_SRC1_ALPHA(18),
}

enum class VkBlendOp(val value: Int) {
    VK_BLEND_OP_ADD(0),
    VK_BLEND_OP_SUBTRACT(1),
    VK_BLEND_OP_REVERSE_SUBTRACT(2),
    VK_BLEND_OP_MIN(3),
    VK_BLEND_OP_MAX(4),
}

enum class VkColorComponentFlagBits(val value: Int) {
    VK_COLOR_COMPONENT_R_BIT(0x1),
    VK_COLOR_COMPONENT_G_BIT(0x2),
    VK_COLOR_COMPONENT_B_BIT(0x4),
    VK_COLOR_COMPONENT_A_BIT(0x8),
}

enum class VkLogicOp(val value: Int) {
    VK_LOGIC_OP_CLEAR(0),
    VK_LOGIC_OP_AND(1),
    VK_LOGIC_OP_AND_REVERSE(2),
    VK_LOGIC_OP_COPY(3),
    VK_LOGIC_OP_AND_INVERTED(4),
    VK_LOGIC_OP_NO_OP(5),
    VK_LOGIC_OP_XOR(6),
    VK_LOGIC_OP_OR(7),
    VK_LOGIC_OP_NOR(8),
    VK_LOGIC_OP_EQUIVALENT(9),
    VK_LOGIC_OP_INVERT(10),
    VK_LOGIC_OP_OR_REVERSE(11),
    VK_LOGIC_OP_COPY_INVERTED(12),
    VK_LOGIC_OP_OR_INVERTED(13),
    VK_LOGIC_OP_NAND(14),
    VK_LOGIC_OP_SET(15),
}

enum class VkPhysicalDeviceType(val value: Int) {
    VK_PHYSICAL_DEVICE_TYPE_OTHER(0),
    VK_PHYSICAL_DEVICE_TYPE_INTEGRATED_GPU(1),
    VK_PHYSICAL_DEVICE_TYPE_DISCRETE_GPU(2),
    VK_PHYSICAL_DEVICE_TYPE_VIRTUAL_GPU(3),
    VK_PHYSICAL_DEVICE_TYPE_CPU(4),
}

enum class VkVertexInputRate(val value: Int) {
    VK_VERTEX_INPUT_RATE_VERTEX(0),
    VK_VERTEX_INPUT_RATE_INSTANCE(1),
}

enum class VkSampleCountFlagBits(val value: Int) {
    VK_SAMPLE_COUNT_1_BIT(0x00000001),
    VK_SAMPLE_COUNT_2_BIT(0x00000002),
    VK_SAMPLE_COUNT_4_BIT(0x00000004),
    VK_SAMPLE_COUNT_8_BIT(0x00000008),
    VK_SAMPLE_COUNT_16_BIT(0x00000010),
    VK_SAMPLE_COUNT_32_BIT(0x00000020),
    VK_SAMPLE_COUNT_64_BIT(0x00000040),
}

enum class VkCompareOp(val value: Int) {
    VK_COMPARE_OP_NEVER(0),
    VK_COMPARE_OP_LESS(1),
    VK_COMPARE_OP_EQUAL(2),
    VK_COMPARE_OP_LESS_OR_EQUAL(3),
    VK_COMPARE_OP_GREATER(4),
    VK_COMPARE_OP_NOT_EQUAL(5),
    VK_COMPARE_OP_GREATER_OR_EQUAL(6),
    VK_COMPARE_OP_ALWAYS(7),
}

enum class VkStencilOp(val value: Int) {
    VK_STENCIL_OP_KEEP(0),
    VK_STENCIL_OP_ZERO(1),
    VK_STENCIL_OP_REPLACE(2),
    VK_STENCIL_OP_INCREMENT_AND_CLAMP(3),
    VK_STENCIL_OP_DECREMENT_AND_CLAMP(4),
    VK_STENCIL_OP_INVERT(5),
    VK_STENCIL_OP_INCREMENT_AND_WRAP(6),
    VK_STENCIL_OP_DECREMENT_AND_WRAP(7),
}

enum class VkDynamicState(val value: Int) {
    VK_DYNAMIC_STATE_VIEWPORT(0),
    VK_DYNAMIC_STATE_SCISSOR(1),
    VK_DYNAMIC_STATE_LINE_WIDTH(2),
    VK_DYNAMIC_STATE_DEPTH_BIAS(3),
    VK_DYNAMIC_STATE_BLEND_CONSTANTS(4),
    VK_DYNAMIC_STATE_DEPTH_BOUNDS(5),
    VK_DYNAMIC_STATE_STENCIL_COMPARE_MASK(6),
    VK_DYNAMIC_STATE_STENCIL_WRITE_MASK(7),
    VK_DYNAMIC_STATE_STENCIL_REFERENCE(8),
}

enum class VkDescriptorType(val value: Int) {
    VK_DESCRIPTOR_TYPE_SAMPLER(0),
    VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER(1),
    VK_DESCRIPTOR_TYPE_SAMPLED_IMAGE(2),
    VK_DESCRIPTOR_TYPE_STORAGE_IMAGE(3),
    VK_DESCRIPTOR_TYPE_UNIFORM_TEXEL_BUFFER(4),
    VK_DESCRIPTOR_TYPE_STORAGE_TEXEL_BUFFER(5),
    VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER(6),
    VK_DESCRIPTOR_TYPE_STORAGE_BUFFER(7),
    VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER_DYNAMIC(8),
    VK_DESCRIPTOR_TYPE_STORAGE_BUFFER_DYNAMIC(9),
    VK_DESCRIPTOR_TYPE_INPUT_ATTACHMENT(10),
}

enum class VkShaderStageFlagBits(val value: Int) {
    VK_SHADER_STAGE_VERTEX_BIT(0x00000001),
    VK_SHADER_STAGE_TESSELLATION_CONTROL_BIT(0x00000002),
    VK_SHADER_STAGE_TESSELLATION_EVALUATION_BIT(0x00000004),
    VK_SHADER_STAGE_GEOMETRY_BIT(0x00000008),
    VK_SHADER_STAGE_FRAGMENT_BIT(0x00000010),
    VK_SHADER_STAGE_COMPUTE_BIT(0x00000020),
    VK_SHADER_STAGE_ALL_GRAPHICS(0x0000001F),
    VK_SHADER_STAGE_ALL(0x7FFFFFFF),
    VK_SHADER_STAGE_RAYGEN_BIT_NV(0x00000100),
    VK_SHADER_STAGE_ANY_HIT_BIT_NV(0x00000200),
    VK_SHADER_STAGE_CLOSEST_HIT_BIT_NV(0x00000400),
    VK_SHADER_STAGE_MISS_BIT_NV(0x00000800),
    VK_SHADER_STAGE_INTERSECTION_BIT_NV(0x00001000),
    VK_SHADER_STAGE_CALLABLE_BIT_NV(0x00002000),
    VK_SHADER_STAGE_TASK_BIT_NV(0x00000040),
    VK_SHADER_STAGE_MESH_BIT_NV(0x00000080),
}

enum class VkFilter(val value: Int) {
    VK_FILTER_NEAREST(0),
    VK_FILTER_LINEAR(1),
}

enum class VkSamplerMipmapMode(val value: Int) {
    VK_SAMPLER_MIPMAP_MODE_NEAREST(0),
    VK_SAMPLER_MIPMAP_MODE_LINEAR(1),
}

enum class VkSamplerAddressMode(val value: Int) {
    VK_SAMPLER_ADDRESS_MODE_REPEAT(0),
    VK_SAMPLER_ADDRESS_MODE_MIRRORED_REPEAT(1),
    VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_EDGE(2),
    VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_BORDER(3),
    VK_SAMPLER_ADDRESS_MODE_MIRROR_CLAMP_TO_EDGE(4),
}

enum class VkBorderColor(val value: Int) {
    VK_BORDER_COLOR_FLOAT_TRANSPARENT_BLACK(0),
    VK_BORDER_COLOR_INT_TRANSPARENT_BLACK(1),
    VK_BORDER_COLOR_FLOAT_OPAQUE_BLACK(2),
    VK_BORDER_COLOR_INT_OPAQUE_BLACK(3),
    VK_BORDER_COLOR_FLOAT_OPAQUE_WHITE(4),
    VK_BORDER_COLOR_INT_OPAQUE_WHITE(5),
}

enum class VkDependencyFlagBits(val value: Int) {
    VK_DEPENDENCY_BY_REGION_BIT(0x00000001),
    VK_DEPENDENCY_DEVICE_GROUP_BIT(0x00000004),
    VK_DEPENDENCY_VIEW_LOCAL_BIT(0x00000002),
}

enum class VkQueueFlagBits(val value: Int) {
    VK_QUEUE_GRAPHICS_BIT(0x00000001),
    VK_QUEUE_COMPUTE_BIT(0x00000002),
    VK_QUEUE_TRANSFER_BIT(0x00000004),
    VK_QUEUE_SPARSE_BINDING_BIT(0x00000008),
    VK_QUEUE_PROTECTED_BIT(0x00000010),
}

enum class VkSurfaceTransformFlagBitsKHR(val value: Int) {
    VK_SURFACE_TRANSFORM_IDENTITY_BIT_KHR(0x00000001),
    VK_SURFACE_TRANSFORM_ROTATE_90_BIT_KHR(0x00000002),
    VK_SURFACE_TRANSFORM_ROTATE_180_BIT_KHR(0x00000004),
    VK_SURFACE_TRANSFORM_ROTATE_270_BIT_KHR(0x00000008),
    VK_SURFACE_TRANSFORM_HORIZONTAL_MIRROR_BIT_KHR(0x00000010),
    VK_SURFACE_TRANSFORM_HORIZONTAL_MIRROR_ROTATE_90_BIT_KHR(0x00000020),
    VK_SURFACE_TRANSFORM_HORIZONTAL_MIRROR_ROTATE_180_BIT_KHR(0x00000040),
    VK_SURFACE_TRANSFORM_HORIZONTAL_MIRROR_ROTATE_270_BIT_KHR(0x00000080),
    VK_SURFACE_TRANSFORM_INHERIT_BIT_KHR(0x00000100),
}

enum class VkCompositeAlphaFlagBitsKHR(val value: Int) {
    VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR(0x00000001),
    VK_COMPOSITE_ALPHA_PRE_MULTIPLIED_BIT_KHR(0x00000002),
    VK_COMPOSITE_ALPHA_POST_MULTIPLIED_BIT_KHR(0x00000004),
    VK_COMPOSITE_ALPHA_INHERIT_BIT_KHR(0x00000008),
}

enum class VkImageUsageFlagBits(val value: Int) {
    VK_IMAGE_USAGE_TRANSFER_SRC_BIT(0x00000001),
    VK_IMAGE_USAGE_TRANSFER_DST_BIT(0x00000002),
    VK_IMAGE_USAGE_SAMPLED_BIT(0x00000004),
    VK_IMAGE_USAGE_STORAGE_BIT(0x00000008),
    VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT(0x00000010),
    VK_IMAGE_USAGE_DEPTH_STENCIL_ATTACHMENT_BIT(0x00000020),
    VK_IMAGE_USAGE_TRANSIENT_ATTACHMENT_BIT(0x00000040),
    VK_IMAGE_USAGE_INPUT_ATTACHMENT_BIT(0x00000080),
    VK_IMAGE_USAGE_SHADING_RATE_IMAGE_BIT_NV(0x00000100),
    VK_IMAGE_USAGE_FRAGMENT_DENSITY_MAP_BIT_EXT(0x00000200),
}

enum class VkSharingMode(val value: Int) {
    VK_SHARING_MODE_EXCLUSIVE(0),
    VK_SHARING_MODE_CONCURRENT(1),
}

enum class VkImageViewType(val value: Int) {
    VK_IMAGE_VIEW_TYPE_1D(0),
    VK_IMAGE_VIEW_TYPE_2D(1),
    VK_IMAGE_VIEW_TYPE_3D(2),
    VK_IMAGE_VIEW_TYPE_CUBE(3),
    VK_IMAGE_VIEW_TYPE_1D_ARRAY(4),
    VK_IMAGE_VIEW_TYPE_2D_ARRAY(5),
    VK_IMAGE_VIEW_TYPE_CUBE_ARRAY(6),
}

enum class VkComponentSwizzle(val value: Int) {
    VK_COMPONENT_SWIZZLE_IDENTITY(0),
    VK_COMPONENT_SWIZZLE_ZERO(1),
    VK_COMPONENT_SWIZZLE_ONE(2),
    VK_COMPONENT_SWIZZLE_R(3),
    VK_COMPONENT_SWIZZLE_G(4),
    VK_COMPONENT_SWIZZLE_B(5),
    VK_COMPONENT_SWIZZLE_A(6),
}

enum class VkImageAspectFlagBits(val value: Int) {
    VK_IMAGE_ASPECT_COLOR_BIT(0x1),
    VK_IMAGE_ASPECT_DEPTH_BIT(0x2),
    VK_IMAGE_ASPECT_STENCIL_BIT(0x4),
    VK_IMAGE_ASPECT_METADATA_BIT(0x8),
}

enum class VkResult(val value: Int) {
    VK_SUCCESS(0),
    VK_NOT_READY(1),
    VK_TIMEOUT(2),
    VK_EVENT_SET(3),
    VK_EVENT_RESET(4),
    VK_INCOMPLETE(5),
    VK_ERROR_OUT_OF_HOST_MEMORY(-1),
    VK_ERROR_OUT_OF_DEVICE_MEMORY(-2),
    VK_ERROR_INITIALIZATION_FAILED(-3),
    VK_ERROR_DEVICE_LOST(-4),
    VK_ERROR_MEMORY_MAP_FAILED(-5),
    VK_ERROR_LAYER_NOT_PRESENT(-6),
    VK_ERROR_EXTENSION_NOT_PRESENT(-7),
    VK_ERROR_FEATURE_NOT_PRESENT(-8),
    VK_ERROR_INCOMPATIBLE_DRIVER(-9),
    VK_ERROR_TOO_MANY_OBJECTS(-10),
    VK_ERROR_FORMAT_NOT_SUPPORTED(-11),
    VK_ERROR_FRAGMENTED_POOL(-12),
}

enum class VkBufferUsageFlagBits(val value: Int) {
    VK_BUFFER_USAGE_TRANSFER_SRC_BIT(0x00000001),
    VK_BUFFER_USAGE_TRANSFER_DST_BIT(0x00000002),
    VK_BUFFER_USAGE_UNIFORM_TEXEL_BUFFER_BIT(0x00000004),
    VK_BUFFER_USAGE_STORAGE_TEXEL_BUFFER_BIT(0x00000008),
    VK_BUFFER_USAGE_UNIFORM_BUFFER_BIT(0x00000010),
    VK_BUFFER_USAGE_STORAGE_BUFFER_BIT(0x00000020),
    VK_BUFFER_USAGE_INDEX_BUFFER_BIT(0x00000040),
    VK_BUFFER_USAGE_VERTEX_BUFFER_BIT(0x00000080),
    VK_BUFFER_USAGE_INDIRECT_BUFFER_BIT(0x00000100),
    VK_BUFFER_USAGE_TRANSFORM_FEEDBACK_BUFFER_BIT_EXT(0x00000800),
    VK_BUFFER_USAGE_TRANSFORM_FEEDBACK_COUNTER_BUFFER_BIT_EXT(0x00001000),
    VK_BUFFER_USAGE_CONDITIONAL_RENDERING_BIT_EXT(0x00000200),
    VK_BUFFER_USAGE_RAY_TRACING_BIT_NV(0x00000400),
    VK_BUFFER_USAGE_SHADER_DEVICE_ADDRESS_BIT_EXT(0x00020000),
}

enum class VkQueryControlFlagBits(val value: Int) {
    VK_QUERY_CONTROL_PRECISE_BIT(0x00000001),
}

enum class VkQueryPipelineStatisticFlagBits(val value: Int) {
    VK_QUERY_PIPELINE_STATISTIC_INPUT_ASSEMBLY_VERTICES_BIT(0x00000001),
    VK_QUERY_PIPELINE_STATISTIC_INPUT_ASSEMBLY_PRIMITIVES_BIT(0x00000002),
    VK_QUERY_PIPELINE_STATISTIC_VERTEX_SHADER_INVOCATIONS_BIT(0x00000004),
    VK_QUERY_PIPELINE_STATISTIC_GEOMETRY_SHADER_INVOCATIONS_BIT(0x00000008),
    VK_QUERY_PIPELINE_STATISTIC_GEOMETRY_SHADER_PRIMITIVES_BIT(0x00000010),
    VK_QUERY_PIPELINE_STATISTIC_CLIPPING_INVOCATIONS_BIT(0x00000020),
    VK_QUERY_PIPELINE_STATISTIC_CLIPPING_PRIMITIVES_BIT(0x00000040),
    VK_QUERY_PIPELINE_STATISTIC_FRAGMENT_SHADER_INVOCATIONS_BIT(0x00000080),
    VK_QUERY_PIPELINE_STATISTIC_TESSELLATION_CONTROL_SHADER_PATCHES_BIT(0x00000100),
    VK_QUERY_PIPELINE_STATISTIC_TESSELLATION_EVALUATION_SHADER_INVOCATIONS_BIT(0x00000200),
    VK_QUERY_PIPELINE_STATISTIC_COMPUTE_SHADER_INVOCATIONS_BIT(0x00000400),
}

enum class VkAccessFlagBits(val value: Int) {
    VK_ACCESS_INDIRECT_COMMAND_READ_BIT(0x00000001),
    VK_ACCESS_INDEX_READ_BIT(0x00000002),
    VK_ACCESS_VERTEX_ATTRIBUTE_READ_BIT(0x00000004),
    VK_ACCESS_UNIFORM_READ_BIT(0x00000008),
    VK_ACCESS_INPUT_ATTACHMENT_READ_BIT(0x00000010),
    VK_ACCESS_SHADER_READ_BIT(0x00000020),
    VK_ACCESS_SHADER_WRITE_BIT(0x00000040),
    VK_ACCESS_COLOR_ATTACHMENT_READ_BIT(0x00000080),
    VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT(0x00000100),
    VK_ACCESS_DEPTH_STENCIL_ATTACHMENT_READ_BIT(0x00000200),
    VK_ACCESS_DEPTH_STENCIL_ATTACHMENT_WRITE_BIT(0x00000400),
    VK_ACCESS_TRANSFER_READ_BIT(0x00000800),
    VK_ACCESS_TRANSFER_WRITE_BIT(0x00001000),
    VK_ACCESS_HOST_READ_BIT(0x00002000),
    VK_ACCESS_HOST_WRITE_BIT(0x00004000),
    VK_ACCESS_MEMORY_READ_BIT(0x00008000),
    VK_ACCESS_MEMORY_WRITE_BIT(0x00010000),
    VK_ACCESS_TRANSFORM_FEEDBACK_WRITE_BIT_EXT(0x02000000),
    VK_ACCESS_TRANSFORM_FEEDBACK_COUNTER_READ_BIT_EXT(0x04000000),
    VK_ACCESS_TRANSFORM_FEEDBACK_COUNTER_WRITE_BIT_EXT(0x08000000),
    VK_ACCESS_CONDITIONAL_RENDERING_READ_BIT_EXT(0x00100000),
    VK_ACCESS_COMMAND_PROCESS_READ_BIT_NVX(0x00020000),
    VK_ACCESS_COMMAND_PROCESS_WRITE_BIT_NVX(0x00040000),
    VK_ACCESS_COLOR_ATTACHMENT_READ_NONCOHERENT_BIT_EXT(0x00080000),
    VK_ACCESS_SHADING_RATE_IMAGE_READ_BIT_NV(0x00800000),
    VK_ACCESS_ACCELERATION_STRUCTURE_READ_BIT_NV(0x00200000),
    VK_ACCESS_ACCELERATION_STRUCTURE_WRITE_BIT_NV(0x00400000),
    VK_ACCESS_FRAGMENT_DENSITY_MAP_READ_BIT_EXT(0x01000000),
}

enum class VkSubpassContents(val value: Int) {
    VK_SUBPASS_CONTENTS_INLINE(0),
    VK_SUBPASS_CONTENTS_SECONDARY_COMMAND_BUFFERS(1),
}

enum class VkCommandBufferUsageFlagBits(val value: Int) {
    VK_COMMAND_BUFFER_USAGE_ONE_TIME_SUBMIT_BIT(0x00000001),
    VK_COMMAND_BUFFER_USAGE_RENDER_PASS_CONTINUE_BIT(0x00000002),
    VK_COMMAND_BUFFER_USAGE_SIMULTANEOUS_USE_BIT(0x00000004),
}

enum class VkMemoryPropertyFlagBits(val value: Int) {
    VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT(0x00000001),
    VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT(0x00000002),
    VK_MEMORY_PROPERTY_HOST_COHERENT_BIT(0x00000004),
    VK_MEMORY_PROPERTY_HOST_CACHED_BIT(0x00000008),
    VK_MEMORY_PROPERTY_LAZILY_ALLOCATED_BIT(0x00000010),
    VK_MEMORY_PROPERTY_PROTECTED_BIT(0x00000020),
}

fun checkError(err: Int, free: (() -> Unit)? = null) {
    if (err != VkResult.VK_SUCCESS.value) {
        free?.invoke()
        throw VkError(err)
    }
}