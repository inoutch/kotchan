package io.github.inoutch.kotchan.test.utillity.mock

import io.github.inoutch.kotchan.core.graphic.material.Material
import io.github.inoutch.kotchan.test.utillity.logger.TestLogger

class MockMaterial(val testLogger: TestLogger) : Material {
    override fun bind() {
        testLogger.log(this, "bind")
    }
}