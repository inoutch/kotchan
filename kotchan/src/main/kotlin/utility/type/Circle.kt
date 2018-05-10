package utility.type

data class Circle(val center: Vector2, val radius: Float) {
    companion object {
        fun collision(c1: Circle, c2: Circle): Boolean {
            val d = c1.center - c2.center
            val r = c1.radius + c2.radius
            return d.x * d.x + d.y * d.y < r * r
        }
    }
}