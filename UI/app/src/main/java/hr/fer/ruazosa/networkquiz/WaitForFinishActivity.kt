package hr.fer.ruazosa.networkquiz

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import hr.fer.ruazosa.networkquiz.net.RestFactory

class WaitForFinishActivity : AppCompatActivity() {

    lateinit var receiver: BroadcastReceiver

    var gameId: String? = null
    var score: Long? = null
    var correct: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wait_for_finish)

        score = intent.getSerializableExtra("score") as Long
        gameId = intent.getSerializableExtra("game_id") as String
        correct = intent.getSerializableExtra("correct") as Long

        PostResult().execute(gameId?.toLong())
        val winnerIntent: Intent = Intent(this, WinnerActivity::class.java)
        receiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action

                Log.i("Receiver", "Broadcast received: $action")

                if(action.equals("winner")) {
                    val username = intent?.getStringExtra("username")
                    val score = intent?.getStringExtra("score")
                    //val correct = intent?.getStringExtra("correct")
                     // (NewGame -> Category -> Opponents)
                    winnerIntent.putExtra("username", username)
                    winnerIntent.putExtra("score", score)
                    startActivity(winnerIntent)
                    finish()
                }
            }
        }
    }

    private fun loadUserId(): Long?{
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("USER_ID", 0)
    }

    private inner class PostResult: AsyncTask<Long, Void, Boolean?>() {

        override fun doInBackground(vararg params: Long?): Boolean? {
            val rest = RestFactory.instance
            val userId = loadUserId()!!
            Log.w("Parametri: ", score.toString() + " "+ correct.toString())
            return params[0]?.let { rest.postResult(it, userId, score!!.toInt(), correct!!.toInt()) }
        }

    }

    public override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            receiver,
            IntentFilter("winner")
        )
    }


    public override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)

    }

    public override fun onBackPressed() {
    }
}