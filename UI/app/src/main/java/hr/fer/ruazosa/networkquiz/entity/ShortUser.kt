package hr.fer.ruazosa.networkquiz.entity

import java.io.Serializable

data class ShortUser (
    var username: String,
    var password: String
): Serializable{
    constructor(u: User): this(
        u.username,
        u.password
    )
}