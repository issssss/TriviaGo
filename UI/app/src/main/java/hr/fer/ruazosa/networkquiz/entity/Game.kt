package hr.fer.ruazosa.networkquiz.entity

import java.io.Serializable

data class Game (
    var questions : List<Question>,
    var players : List<User>,
    var pending : Int,
    var finished: Int
): Serializable{
    var gameId: Int? = null
}
