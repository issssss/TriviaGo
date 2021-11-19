package hr.fer.ruazosa.networkquiz.entity

import java.io.Serializable
data class RankUser (
    var rank: Integer,
    var username: String,
    var points: Integer
): Serializable