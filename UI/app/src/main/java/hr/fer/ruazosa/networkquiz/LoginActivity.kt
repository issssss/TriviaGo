package hr.fer.ruazosa.networkquiz

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import hr.fer.ruazosa.networkquiz.entity.ShortUser
import hr.fer.ruazosa.networkquiz.entity.User
import hr.fer.ruazosa.networkquiz.net.RestFactory
import kotlinx.android.synthetic.main.activity_login.*
import retrofit.RetrofitError


class LoginActivity : AppCompatActivity() {

    var errorMessage : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            //finish()
        }

        loginButton.setOnClickListener {
            val username: String = usernameEditText.text.toString()
            val password: String = passwordEditText.text.toString()

            val user = ShortUser(
                username,
                password
            )

            LoginUser().execute(user)


        }
    }

    private inner class LoginUser : AsyncTask<ShortUser, Void, User?>(){

        override fun doInBackground(vararg user: ShortUser): User? {
            val rest = RestFactory.instance
            return try{
                val logUser = rest.loginUser(user[0])
                logUser
            } catch (e: RetrofitError){
                errorMessage = e.toString()
                null
            }
        }

        override fun onPostExecute(user: User?) {
            val rest = RestFactory.instance
            if (user != null) {

                FirebaseMessaging.getInstance().token.addOnCompleteListener(
                    OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            //Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                            return@OnCompleteListener
                        }

                        // Get new FCM registration token
                        val token = task.result
                        if (user.token != token.toString()) {
                            UpdateToken().execute(user.username, token.toString())//ovo dovrsiti

                        }
                    })
                saveUsername(user)

                val intent = Intent(this@LoginActivity, MyProfileActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
            } else {
                Log.d("ERROR MESSAGE", errorMessage)
                when(errorMessage){
                    "retrofit.RetrofitError: 406 " -> Toast.makeText(
                        applicationContext, "username or password are empty",
                        Toast.LENGTH_LONG).show()
                    "retrofit.RetrofitError: 404 " -> Toast.makeText(
                        applicationContext, "no user found",
                        Toast.LENGTH_SHORT).show()
                    else -> {
                        Toast.makeText(
                            applicationContext, "Login failed!",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    private fun saveUsername(user: User) {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("USERNAME", user.username)
            putString("PASSWORD", user.password)
            putLong("USER_ID", user.id!!.toLong())
        }.apply()
    }

    private inner class UpdateToken : AsyncTask<String, Void, Long>() {


        override fun doInBackground(vararg p0: String): Long? {
            var rest = RestFactory.instance
            return rest.setNewToken(p0[0], p0[1])
        }
    }
}