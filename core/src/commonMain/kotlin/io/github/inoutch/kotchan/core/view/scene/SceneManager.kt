package io.github.inoutch.kotchan.core.view.scene

class SceneManager {
    private val context = SceneContext()

    private var currentScene: Scene? = null

    private var nextSceneCreateCallback: ((context: SceneContext) -> Scene)? = null

    suspend fun render(delta: Float) {
        nextSceneCreateCallback?.let {
            currentScene = it.invoke(context)
        }

        currentScene?.let {
            it.update(delta)
            it.render(delta)
        }
    }

    suspend fun transitScene(sceneCreateCallback: (context: SceneContext) -> Scene) {
        nextSceneCreateCallback = sceneCreateCallback
    }
}
