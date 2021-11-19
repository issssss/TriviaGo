package hr.fer.ruazosa.networkquiz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import hr.fer.ruazosa.networkquiz.entity.*
import hr.fer.ruazosa.networkquiz.net.RestFactory
import kotlinx.android.synthetic.main.activity_leaderboard.*
import kotlinx.android.synthetic.main.activity_leaderboard.returnButton

class LeaderboardActivity : AppCompatActivity() {
    lateinit var adapter : LeaderboardUserAdapter
    lateinit var data : MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        val user = intent.getSerializableExtra("user") as? User

        data = mutableListOf()
        GetLeaderBoard().execute()

        returnButton.setOnClickListener {
            val returnIntent = Intent(this, MyProfileActivity::class.java)
            returnIntent.putExtra("user",user)
            onBackPressed()
            finish()
        }

        leaderboardUsersRecyclerView.layoutManager = LinearLayoutManager(application)
        adapter = LeaderboardUserAdapter(data)
        leaderboardUsersRecyclerView.adapter = adapter
        val decorator = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
        decorator.setDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.decorator1)!!)
        leaderboardUsersRecyclerView.addItemDecoration(decorator)
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetLeaderBoard:AsyncTask<String, Void, List<User>?>(){

        override fun doInBackground(vararg usernameToExclude: String): List<User>? {
            val rest = RestFactory.instance
            return rest.getLeaderboard()
        }

        override fun onPostExecute(users: List<User>?) {
            if (users != null) {
                this@LeaderboardActivity.data.addAll(users)
                adapter.notifyDataSetChanged()
            }

        }
    }
}
