package io.github.inoutch.kotchan.core.model

import io.github.inoutch.kotchan.core.destruction.StrictDestruction

open class ModelManager : StrictDestruction(), Model {

    protected val models: MutableMap<Model, ModelDestructionType> = mutableMapOf()

    fun add(model: Model, destructionType: ModelDestructionType = ModelDestructionType.AutoDestruction) {
        models[model] = destructionType
    }

    fun clear() {
        models.forEach { destroyModel(it.key, it.value) }
        models.clear()
    }

    fun remove(model: Model) {
        destroyModel(model, models[model] ?: return)
        models.remove(model)
    }

    override fun update(delta: Float) {
        models.keys.forEach { it.update(delta) }
    }

    override fun destroy() {
        super.destroy()
        clear()
    }

    private fun destroyModel(model: Model, destructionType: ModelDestructionType) {
        if (destructionType == ModelDestructionType.AutoDestruction) {
            model.destroy()
        }
    }
}