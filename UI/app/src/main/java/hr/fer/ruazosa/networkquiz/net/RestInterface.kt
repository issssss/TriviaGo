package hr.fer.ruazosa.networkquiz.net

import hr.fer.ruazosa.networkquiz.entity.*

interface RestInterface {
    fun registerUser(user: User): User?
    fun loginUser(user: ShortUser): User?
    fun getUserRank(username: String): User?
    fun getCategories(count: Int): List<Category>?
    fun getOpponents(usernameToExclude: String) : List<User>?
    fun getUserToken(username: String): String?
    fun setNewToken(username: String, token: String): Long?
    fun getQuestions(categoryId: Int): CatQuestions?
    fun getQuestionsFromGame(gameId:Long): List<Question>?
    fun createNewGame(game: Game, username: String): Boolean?
    fun joinGameResponse(gameId: Long, response: Boolean, userId: Long): Boolean?
    fun getLeaderboard(): List<User>?
    fun postResult(gameId: Long, userId: Long, score: Int, correct: Int): Boolean?
}