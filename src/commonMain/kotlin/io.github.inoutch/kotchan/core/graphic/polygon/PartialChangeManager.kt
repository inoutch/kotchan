package io.github.inoutch.kotchan.core.graphic.polygon

class PartialChangeManager<T> {
    private val changeMap = mutableMapOf<Int, T>()

    fun add(changes: List<T>, offset: Int) {
        changes.forEachIndexed { index, x -> changeMap[offset + index] = x }
    }

    fun clear() {
        changeMap.clear()
    }

    fun blocks(): List<PartialChange<List<T>>> {
        if (changeMap.isEmpty()) {
            return emptyList()
        }

        val sortedKeys = changeMap.keys.sorted()
        var index = sortedKeys.first()
        var currentOffset = index
        val block = mutableListOf<T>()
        val blocks = mutableListOf<PartialChange<List<T>>>()

        for (x in sortedKeys) {
            if (x != index) {
                blocks.add(PartialChange(block.toList(), currentOffset))
                currentOffset = x
                index = x + 1
                block.clear()
            } else {
                index++
            }

            block.add(changeMap.getValue(x))
        }
        blocks.add(PartialChange(block.toList(), currentOffset))
        return blocks
    }
}
