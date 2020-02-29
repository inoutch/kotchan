package io.github.inoutch.kotchan.core.view.scene

class SceneManager {
    private val context = SceneContext(this)

    private var currentScene: Scene? = null

    private var nextSceneCreateCallback: ((context: SceneContext) -> Scene)? = null

    suspend fun render(delta: Float) {
        nextSceneCreateCallback?.let {
            nextSceneCreateCallback = null
            currentScene = it.invoke(context).apply { init() }
        }

        currentScene?.let {
            it.update(delta)
            it.render(delta)
        }
    }

    fun transitScene(sceneCreateCallback: (context: SceneContext) -> Scene) {
        nextSceneCreateCallback = sceneCreateCallback
    }
}
