package hr.fer.ruazosa.networkquiz

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import hr.fer.ruazosa.networkquiz.entity.Game
import hr.fer.ruazosa.networkquiz.entity.Question
import hr.fer.ruazosa.networkquiz.entity.RunnableGame
import hr.fer.ruazosa.networkquiz.net.RestFactory
import kotlinx.android.synthetic.main.activity_my_profile.*


class WaitForGameStart : AppCompatActivity() {

    lateinit var receiver: BroadcastReceiver
    var questions: List<Question>?=null
    lateinit var gamedata: RunnableGame
    var gameId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wait_for_game_start)


        receiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action

                Log.i("Receiver", "Broadcast received: $action")

                if(action.equals("begin")) {
                    gameId = intent?.getStringExtra("game_id")
                    Log.d("GAMEID","TEST")

                    // Get Questions
                    Questions().execute(gameId?.toLong())

                } else if(action.equals("stop")){
                    var message = Toast.makeText(applicationContext,"Nobody is available to join :(", Toast.LENGTH_LONG)
                    message.show()
                    val profileIntent = Intent(applicationContext, MyProfileActivity::class.java)
                    startActivity(profileIntent)
                    finish()
                }
            }
        }
    }

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
                gamedata = RunnableGame(questions!!,gameId?.toInt()!!)
                startGameIntent.putExtra("gamedata", gamedata)
                startGameIntent.putExtra("game_id", gameId)
                startActivity(startGameIntent)
                finish()
            }
        }

    }

    public override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            receiver,
            IntentFilter("begin")
        )
        LocalBroadcastManager.getInstance(this).registerReceiver(
            receiver,
            IntentFilter("stop")
        )
    }


    public override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)

    }

    public override fun onBackPressed() {
    }
}
