package hr.fer.ruazosa.networkquiz.entity

import java.io.Serializable
data class Question (
    var answer: String,
    var question: String
): Serializable{
    var id: Int? = null
}