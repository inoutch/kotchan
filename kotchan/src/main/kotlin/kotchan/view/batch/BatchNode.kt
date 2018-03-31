package kotchan.view.batch

data class BatchNode(
        val positionBufferData: BatchBufferData,
        val colorBufferData: BatchBufferData,
        val texcoordBufferData: BatchBufferData)