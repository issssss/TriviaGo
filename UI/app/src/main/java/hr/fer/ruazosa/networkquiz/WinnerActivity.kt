package hr.fer.ruazosa.networkquiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import hr.fer.ruazosa.networkquiz.entity.User

class WinnerActivity : AppCompatActivity() {

    var usernameTextView: TextView?=null
    var scoreTextView: TextView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_winner)

        val score = intent.getSerializableExtra("score") as String
        val username = intent.getSerializableExtra("username") as String

        usernameTextView = findViewById<TextView?>(R.id.winner_un)
        scoreTextView = findViewById<TextView?>(R.id.score)
        usernameTextView?.text = username
        scoreTextView?.text = score


        val finishButton = findViewById<Button>(R.id.finish_button)
        finishButton.setOnClickListener{
            var profilIntent = Intent(this, MyProfileActivity::class.java)
            startActivity(profilIntent)
            finish()

        }
        }

    private fun loadUserId(): Long?{
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("USER_ID", 0)
    }

    public override fun onBackPressed() {
    }
}