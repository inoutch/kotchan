package utility.type

data class Mesh(
        val vertices: List<Vector3>,
        val texcoords: List<Vector2>,
        val colors: List<Vector4>)