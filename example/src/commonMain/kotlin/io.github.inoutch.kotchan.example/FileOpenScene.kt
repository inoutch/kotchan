package io.github.inoutch.kotchan.example

import io.github.inoutch.kotchan.core.KotchanGlobalContext.Companion.file
import io.github.inoutch.kotchan.core.io.file.readTextFromResourceWithErrorAsync
import io.github.inoutch.kotchan.core.view.scene.Scene
import io.github.inoutch.kotchan.core.view.scene.SceneContext

@ExperimentalStdlibApi
class FileOpenScene(context: SceneContext) : Scene(context) {
    override suspend fun init() {
        val text = file.readTextFromResourceWithErrorAsync("txt/hello_async.txt").await()
        println("Read text asynchronously: $text")
    }

    override suspend fun update(delta: Float) {
    }

    override suspend fun render(delta: Float) {
        println("render")
    }
}
