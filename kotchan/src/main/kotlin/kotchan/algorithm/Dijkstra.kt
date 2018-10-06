package kotchan.algorithm

import kotchan.view.map.converter.TileConverterInterface
import utility.collection.Queue
import utility.type.Point
import kotlin.math.sqrt

class Dijkstra(private val layerBase: TileConverterInterface, private val isWall: (id: Int?) -> Boolean) {
    private val slantingCost = sqrt(2.0f)

    data class Joint(val cost: Float, val p: Point)

    fun search(start: Point, end: Point): List<Point> {
        return search(start, listOf(end))
    }

    fun search(start: Point, ends: List<Point>): List<Point> {
        if (isWall(layerBase.mapId(start.x, start.y))) {
            return emptyList()
        }
        val joints: MutableMap<Int, MutableMap<Int, Joint>> = mutableMapOf()
        val getJoint = { x: Int, y: Int ->
            joints.getOrPut(y) { mutableMapOf() }.getOrElse(x) { null }
        }
        val setJoint = { x: Int, y: Int, joint: Joint ->
            joints.getOrPut(y) { mutableMapOf() }[x] = joint
        }

        val queue: Queue<Point> = Queue()
        queue.enqueue(start)
        setJoint(start.x, start.y, Joint(0.0f, start))
        val lambda = { current: Joint, x: Int, y: Int, dx: Int, dy: Int, cost: Float ->
            val p = Point(x + dx, y + dy)
            val joint = Joint(current.cost + cost, Point(x, y))
            val exist = getJoint(p.x, p.y)
            when {
                exist == null -> {
                    joints[p.y]?.set(p.x, joint)
                    p
                }
                joint.cost < exist.cost -> {
                    joints[p.y]?.set(p.x, joint)
                    p
                }
                else -> null
            }
        }
        var endpoint: Point? = null
        do {
            val p = queue.dequeue() ?: break
            if (ends.any { endpoint = it;p.equals(it) }) {
                break
            }
            val current = getJoint(p.x, p.y) ?: break
            val t = isWall(layerBase.mapId(p.x, p.y + 1))
            val r = isWall(layerBase.mapId(p.x + 1, p.y))
            val d = isWall(layerBase.mapId(p.x, p.y - 1))
            val l = isWall(layerBase.mapId(p.x - 1, p.y))
            if (!t) lambda(current, p.x, p.y, +0, +1, 1.0f)?.let { queue.enqueue(it) }
            if (!r) lambda(current, p.x, p.y, +1, +0, 1.0f)?.let { queue.enqueue(it) }
            if (!d) lambda(current, p.x, p.y, +0, -1, 1.0f)?.let { queue.enqueue(it) }
            if (!l) lambda(current, p.x, p.y, -1, +0, 1.0f)?.let { queue.enqueue(it) }

            if (!t && !r && !isWall(layerBase.mapId(p.x + 1, p.y + 1)))
                lambda(current, p.x, p.y, +1, +1, slantingCost)?.let { queue.enqueue(it) }
            if (!t && !l && !isWall(layerBase.mapId(p.x - 1, p.y + 1)))
                lambda(current, p.x, p.y, -1, +1, slantingCost)?.let { queue.enqueue(it) }
            if (!d && !r && !isWall(layerBase.mapId(p.x + 1, p.y - 1)))
                lambda(current, p.x, p.y, +1, -1, slantingCost)?.let { queue.enqueue(it) }
            if (!d && !l && !isWall(layerBase.mapId(p.x - 1, p.y - 1)))
                lambda(current, p.x, p.y, -1, -1, slantingCost)?.let { queue.enqueue(it) }
        } while (true)
        var notNullEndpoint = endpoint ?: return emptyList()
        val lists = MutableList(1) { notNullEndpoint }
        do {
            val joint = getJoint(notNullEndpoint.x, notNullEndpoint.y) ?: return emptyList()
            if (notNullEndpoint.equals(joint.p)) {
                return lists.reversed()
            }
            lists.add(joint.p)
            notNullEndpoint = joint.p
        } while (true)
    }
}