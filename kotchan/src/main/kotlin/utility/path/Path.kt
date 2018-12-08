package utility.path

class Path {
    companion object {
        fun dir(vararg dirs: String): String {
            val prefix = if (dirs[0].startsWith("/")) "/" else ""
            return prefix + dirs.map { it.split("/") }
                    .flatten()
                    .filterNot { it == "" }
                    .joinToString("/")
        }
    }
}