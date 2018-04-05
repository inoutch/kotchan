package kotchan.scene.batch

data class BatchNode(
        val positionBufferData: BatchBufferData,
        val colorBufferData: BatchBufferData,
        val texcoordBufferData: BatchBufferData)