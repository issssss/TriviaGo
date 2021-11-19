package hr.fer.ruazosa.networkquiz

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fer.ruazosa.networkquiz.entity.User
import hr.fer.ruazosa.networkquiz.net.RestFactory
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfileActivity : AppCompatActivity() {

    lateinit var user : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)


        //usernameTextView?.text = user?.username

        UserRank().execute(loadUsername())

        startNewGameButton.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java) //ovisi u kojem redosljedu ide; kategorija -> igraci -> pitanja?
            intent.putExtra("user", user)
            startActivity(intent)
        }
        leaderboardButton.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }

    }

    private fun loadUsername(): String?{
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("USERNAME", null)
    }

    private inner class UserRank: AsyncTask<String, Void, User?>() {

        override fun doInBackground(vararg user: String): User? {
            val rest = RestFactory.instance
            return rest.getUserRank(user[0])

        }

        override fun onPostExecute(user: User?) {
            if (user != null) {
                this@MyProfileActivity.user = user
            }
            this@MyProfileActivity.usernameTextView.text = user?.username
            positionNumberView?.text = user?.rank.toString()
            gamesNumberView?.text = user?.gamesPlayed.toString()
            accuracyPercentageView?.text = user?.correct.toString()
            pointsNumberView?.text = user?.score.toString()
        }
    }

}