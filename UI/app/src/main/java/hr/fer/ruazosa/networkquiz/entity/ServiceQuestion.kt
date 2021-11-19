package hr.fer.ruazosa.networkquiz.entity

import java.io.Serializable

data class ServiceQuestion (
    var id: Int,
    var answer: String,
    var question: String,
    var value: Int,
    var airdate: String,
    var category_id: Int,
    var created_at: String,
    var game_id: Int,
    var invalid_count: Int
):Serializable