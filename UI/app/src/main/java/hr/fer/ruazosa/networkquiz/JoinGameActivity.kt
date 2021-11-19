package hr.fer.ruazosa.networkquiz

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import hr.fer.ruazosa.networkquiz.entity.Question
import hr.fer.ruazosa.networkquiz.entity.RunnableGame
import hr.fer.ruazosa.networkquiz.net.RestFactory
import kotlinx.android.synthetic.main.join_game_dialog.*

class JoinGameActivity : AppCompatActivity() {

    var response: Boolean = true
    var userId: Long? = null

    var questions: List<Question>?=null
    lateinit var gamedata: RunnableGame
    var gameId: String? = null

    private lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_game)

        val message = intent.getStringExtra("message")
        gameId = intent.getStringExtra("game_id")

        userId = loadUserId()

        joinGameTextView.text = message

        joinGameButton.setOnClickListener {
            response = true
            JoinGameResponse().execute(gameId?.toLong())
        }

        declineGameButton.setOnClickListener {
            response = false
            JoinGameResponse().execute(gameId?.toLong())
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }

        receiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action

                Log.i("Receiver", "Broadcast received: $action")

                if(action.equals("begin") && response) {
                    gameId = intent?.getStringExtra("game_id")
                    //TODO get questions and start game
                    Questions().execute(gameId?.toLong())
                }
            }
        }

    }

    private fun loadUserId(): Long?{
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("USER_ID", 0)
    }

    @SuppressLint("StaticFieldLeak")
    private inner class JoinGameResponse: AsyncTask<Long, Void, Boolean?>(){

        override fun doInBackground(vararg id: Long?): Boolean? {
            val rest = RestFactory.instance
            return if(id[0] != null && userId != null){
                rest.joinGameResponse(id[0]!!, response, userId!!)
            } else {
                Log.d("ID", id[0].toString())
                Log.d("RESPONSE", response.toString())
                Log.d("USER_ID", userId.toString())
                false
            }
        }

        override fun onPostExecute(result: Boolean?) {
            if (result != null){
                if(response){
                    val intent = Intent(this@JoinGameActivity, WaitForGameStart::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class Questions: AsyncTask<Long, Void, List<Question>?>(){
        override fun doInBackground(vararg p0: Long?): List<Question>? {
            val rest = RestFactory.instance
            return p0[0]?.let { rest.getQuestionsFromGame(it) } // Get the questions
        }

        override fun onPostExecute(result: List<Question>?) {
            Log.d("RESULT",result.toString())
            if (result != null) {
                questions = result

                // Start the game
                val startGameIntent = Intent(applicationContext,GameActivity::class.java)
                gamedata = RunnableGame(questions!!,gameId!!.toInt())
                startGameIntent.putExtra("gamedata", gamedata)
                startGameIntent.putExtra("game_id", gameId)
                startActivity(startGameIntent)
            }
        }

    }

    public override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            receiver,
            IntentFilter("begin")
        )
    }


    public override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)

    }

    public override fun onBackPressed(){}
}
