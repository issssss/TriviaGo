package hr.fer.ruazosa.networkquiz

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import hr.fer.ruazosa.networkquiz.entity.*
import hr.fer.ruazosa.networkquiz.net.RestFactory
import retrofit2.http.Url
import kotlinx.android.synthetic.main.activity_choose_players.*
import java.util.*

class ChoosePlayersActivity : AppCompatActivity() {

    lateinit var adapter : UserAdapter
    var newGameText: TextView? = null // Unnecessary?
    var searchText : EditText ? = null
    var startGameButton: Button? = null
    lateinit var opponents : MutableList<SelectableUser>

    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_players)

        // Initializing usernames
        opponents = mutableListOf<SelectableUser>()
        user = intent.getSerializableExtra("user") as? User
        var category = intent.getSerializableExtra("category") as? Int
        Users().execute(user?.username)


        returnButton.setOnClickListener{
            val returnIntent = Intent(this, CategoryActivity::class.java) // (NewGame -> Category -> Opponents)
            returnIntent.putExtra("user",user)
            onBackPressed()
            //startActivity(returnIntent)
            //finish()
        }


        usersRecyclerView.layoutManager = LinearLayoutManager(application)

        // Instantiating adapter and adding it to the RecyclerView
        adapter = UserAdapter(opponents)
        usersRecyclerView.adapter = adapter

        // Instantiating decorator/divider and adding it to the RecyclerView
        val decorator = DividerItemDecoration(applicationContext , LinearLayoutManager.VERTICAL)
        decorator.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.decorator1)!!)
        usersRecyclerView.addItemDecoration(decorator)


        // initializing search functionality ( see: filterPlayers , UserAdapter )
        searchText = findViewById(R.id.editSearchText)
        searchText?.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                filterPlayers(p0?.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        startButton?.setOnClickListener {
            if(getSelectedPlayers().size > 1){
                Questions().execute(category)
            }
            else{
                val text = "Select opponents!"
                val toast = Toast.makeText(applicationContext,text,Toast.LENGTH_LONG)
                toast.show()
            }
        }


    }

    private fun getSelectedPlayers(): List<User>{
        var players = mutableListOf<User>()
        for(opponent in opponents){
            if(opponent.selected){
                players.add(opponent.user)
            }
        }
        players.add(user!!)
        return players.toMutableList()
    }

    private fun filterPlayers(filter:String?) {
        var filteredList = mutableListOf<SelectableUser>()
        if (filter != null ){
            for ( tempUser in opponents) {
                if (tempUser.user.username.toLowerCase().contains(filter.toLowerCase()))
                    filteredList.add(tempUser)
            }
        }
        adapter.filterPlayers(filteredList)
    }

    // Get all users EXCEPT the currently logged-in ( all available opponents )
    private inner class Users:AsyncTask< String ,Void , List<User>?>(){

        override fun doInBackground(vararg usernameToExclude: String): List<User>? {
            val rest = RestFactory.instance
            return rest.getOpponents(usernameToExclude[0])
        }

        override fun onPostExecute(users: List<User>?) {
            if (users != null) {
                for (user in users ) {
                    val tempUser = SelectableUser(user, false)
                    opponents.add(tempUser)
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private inner class Questions:AsyncTask<Int ,Void , CatQuestions?>(){

        override fun doInBackground(vararg category: Int?): CatQuestions? {
            val rest = RestFactory.instance
            val cat : CatQuestions? = category[0]?.let { rest.getQuestions(it) }
            Log.w("Dohvaceno", cat.toString())
            return cat
        }

        override fun onPostExecute(questions: CatQuestions?) {
            //create new game
            var i = 0
            var questionForm: MutableList<Question> = mutableListOf()
            for(question in questions?.clues!!){
                if(i > 4) break
                i++
                var saveQuestion = Question(question.answer, question.question)
                questionForm.add(saveQuestion)
            }
            var players = getSelectedPlayers()
            var newGame = Game(questionForm, players, players.size - 1, 0)
            CreateGame().execute(newGame)

        }
    }

    private inner class CreateGame:AsyncTask<Game,Void, Boolean?>(){
        override fun doInBackground(vararg game: Game?): Boolean? {
            val rest = RestFactory.instance
            return game[0]?.let { rest.createNewGame(it, user!!.username) }
        }

        override fun onPostExecute(result: Boolean?){
            if(result != null){
                val intent = Intent(this@ChoosePlayersActivity, WaitForGameStart::class.java)
                startActivity(intent)
            }
        }

    }


}