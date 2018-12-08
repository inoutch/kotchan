package kotchan.algorithm

import utility.collection.Queue
import utility.type.Point
import kotlin.math.sqrt

class Dijkstra(private val isWall: (x: Int, y: Int) -> Boolean) {
    internal data class Joint(val cost: Float, val p: Point)

    private val slantingCost = sqrt(2.0f)

    fun search(start: Point, end: Point): List<Point> {
        return search(start, listOf(end))
    }

    fun search(start: Point, ends: List<Point>): List<Point> {
        if (isWall(start.x, start.y)) {
            return emptyList()
        }

        val joints: MutableMap<Int, MutableMap<Int, Joint>> = mutableMapOf()
        val queue: Queue<Point> = Queue()
        var endpoint: Point? = null

        val getJoint = { x: Int, y: Int ->
            joints.getOrPut(y) { mutableMapOf() }[x]
        }
        val setJoint = { x: Int, y: Int, joint: Joint ->
            joints.getOrPut(y) { mutableMapOf() }[x] = joint
        }

        val nextPoint = { current: Joint, x: Int, y: Int, dx: Int, dy: Int, cost: Float ->
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

        queue.enqueue(start)
        setJoint(start.x, start.y, Joint(0.0f, start))

        do {
            val p = queue.dequeue() ?: break
            if (ends.any { endpoint = it;p.equals(it) }) {
                break
            }
            val current = getJoint(p.x, p.y) ?: break
            val t = isWall(p.x, p.y + 1)
            val r = isWall(p.x + 1, p.y)
            val b = isWall(p.x, p.y - 1)
            val l = isWall(p.x - 1, p.y)
            if (!t) nextPoint(current, p.x, p.y, +0, +1, 1.0f)?.let { queue.enqueue(it) }
            if (!r) nextPoint(current, p.x, p.y, +1, +0, 1.0f)?.let { queue.enqueue(it) }
            if (!b) nextPoint(current, p.x, p.y, +0, -1, 1.0f)?.let { queue.enqueue(it) }
            if (!l) nextPoint(current, p.x, p.y, -1, +0, 1.0f)?.let { queue.enqueue(it) }

            if (!t && !r && !isWall(p.x + 1, p.y + 1))
                nextPoint(current, p.x, p.y, +1, +1, slantingCost)?.let { queue.enqueue(it) }
            if (!t && !l && !isWall(p.x - 1, p.y + 1))
                nextPoint(current, p.x, p.y, -1, +1, slantingCost)?.let { queue.enqueue(it) }
            if (!b && !r && !isWall(p.x + 1, p.y - 1))
                nextPoint(current, p.x, p.y, +1, -1, slantingCost)?.let { queue.enqueue(it) }
            if (!b && !l && !isWall(p.x - 1, p.y - 1))
                nextPoint(current, p.x, p.y, -1, -1, slantingCost)?.let { queue.enqueue(it) }
        } while (true)
        var notNullEndpoint = endpoint ?: return emptyList()
        val lists = mutableListOf(notNullEndpoint)
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