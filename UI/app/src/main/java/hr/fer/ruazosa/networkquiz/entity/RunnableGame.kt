package hr.fer.ruazosa.networkquiz.entity

import java.io.Serializable

data class RunnableGame (
    var questions : List<Question>,
    var originalGameId: Int
): Serializable {
    var runnableGameId: Int? = null
}
