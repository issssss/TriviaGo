package hr.fer.ruazosa.networkquiz.entity

import java.io.Serializable

data class Category (
    var id : Int,
    var title: String,
    var clues_count: Int

): Serializable