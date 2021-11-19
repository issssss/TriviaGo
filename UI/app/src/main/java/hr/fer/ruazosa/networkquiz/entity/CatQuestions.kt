package hr.fer.ruazosa.networkquiz.entity

import java.io.Serializable

data class CatQuestions (
    var id : Int,
    var title: String,
    var created: String,
    var updated: String,
    var clues_count: Int,
    var clues: List<ServiceQuestion>
):Serializable