package hr.fer.ruazosa.networkquiz.net

import com.squareup.okhttp.ResponseBody
import hr.fer.ruazosa.networkquiz.entity.*
import retrofit.http.*

interface  UserService {

    @POST("/registerUser")
    fun registerUser(@Body user: User): User?

    @POST("/loginUser")
    fun loginUser(@Body user: ShortUser): User?

    @GET("/userRank/{id}")
    fun getUserRank(@Path("id") username: String): User?

    @GET("/usernames")
    fun getOpponents(@Query("usernameToExclude") usernameToExclude:String) : List<User>

    @GET("/token/{id}")
    fun getUserToken(@Path("id") username: String): String?

    @PATCH("/token")
    @FormUrlEncoded
    fun setNewToken(@Query("username") username: String, @Field("token") newToken: String): Long?

    @POST("/createNewGame/{user}")
    fun createNewGame(@Body game: Game, @Path("user") username: String): Boolean?

    @PATCH("/joinGame/{id}")
    @FormUrlEncoded
    fun joinGameResponse(@Path("id") gameId: Long, @Field("response") response: Boolean, @Query("user_id") userId: Long): Boolean?

    @GET("/getQuestions/{gameid}")
    fun getQuestionsFromGame(@Path("gameid") gameId:Long): List<Question> ?

    @GET("/users")
    fun getLeaderboard():List<User>

    @POST("/postResult/{id}")
    @FormUrlEncoded
    fun postResult(@Path("id") gameId: Long, @Field("correct")correct: Int, @Query("score") score: Int, @Query("user_id") userId: Long): Boolean?


}