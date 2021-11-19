package hr.fer.ruazosa.networkquiz.net
import hr.fer.ruazosa.networkquiz.entity.CatQuestions
import hr.fer.ruazosa.networkquiz.entity.Category
import retrofit.http.GET
import retrofit.http.Query

interface CategoriesService {

    @get:GET("/categories?count=100")
    val listOfCategories: List<Category>?

    @GET("/category")
    fun getQuestions(@Query("id") categoryId: Int): CatQuestions?
}