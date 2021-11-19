package hr.fer.ruazosa.networkquiz.entity

import java.io.Serializable
data class SelectableUser (
    var user: User,
    var selected:Boolean
): Serializable