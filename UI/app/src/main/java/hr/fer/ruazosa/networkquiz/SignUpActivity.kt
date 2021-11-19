package hr.fer.ruazosa.networkquiz

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hr.fer.ruazosa.networkquiz.net.RestFactory
import android.widget.Toast.LENGTH_SHORT
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import hr.fer.ruazosa.networkquiz.entity.User
import retrofit.RetrofitError


class SignUpActivity : AppCompatActivity() {
    var firstName: TextView? = null
    var lastName: TextView? = null
    var userName: TextView? = null
    var email: TextView? = null
    var password: TextView? = null

    var errorMessage : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firstName = findViewById<TextView?>(R.id.nameSignUp)
        lastName = findViewById<TextView?>(R.id.surnameSignUp)
        userName = findViewById(R.id.usernameSignUp)
        email = findViewById(R.id.emailAddress)
        password = findViewById(R.id.passwordSignUp)


        val returnButton = findViewById<ImageButton>(R.id.returnButton)
        returnButton.setOnClickListener{
            onBackPressed()
            finish()
        }
        val signUpButtNext = findViewById<Button>(R.id.signUpButton)
        signUpButtNext.setOnClickListener {
            val userFirstName: String = firstName?.text.toString()
            val userLastName: String = lastName?.text.toString()
            val userUsername: String = userName?.text.toString()
            val userEmail: String = email?.text.toString()
            val userPassword: String = password?.text.toString()

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                val toastMessage =
                    Toast.makeText(applicationContext, "Invalid e-mail format", Toast.LENGTH_LONG)
                toastMessage.show()

            } else {
                val user = User(
                    userFirstName,
                    userLastName,
                    userUsername,
                    userEmail,
                    userPassword,
                    token = ""
                )
                val instance = FirebaseMessaging.getInstance().token.addOnCompleteListener(
                    OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        //Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result
                        user.token = token.toString()
                })

                RegisterUser().execute(user)
            }
        }
    }

    private inner class RegisterUser: AsyncTask<User, Void, User?>() {

        override fun doInBackground(vararg user: User): User? {
            val rest = RestFactory.instance
            Log.d("USER CORRECT", user[0].correct.toString())
            return try{
                val regUser = rest.registerUser(user[0])
                regUser
            } catch (e: RetrofitError){
                errorMessage = e.toString()
                null
            }
        }

        override fun onPostExecute(user: User?) {
            if (user != null) {

                saveUsername(user)

                val intent = Intent(this@SignUpActivity, MyProfileActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
            }else {
                Log.d("ERROR MESSAGE", errorMessage)
                when(errorMessage){
                    "retrofit.RetrofitError: 406 " -> Toast.makeText(
                        applicationContext, "This username is already taken",
                        Toast.LENGTH_SHORT).show()
                    else -> {
                        Toast.makeText(
                            applicationContext, "Registration failed!",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    private fun saveUsername(user: User){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply{
            putString("USERNAME", user.username)
            putString("PASSWORD", user.password)
            putLong("USER_ID", user.id!!.toLong())
        }.apply()
    }
}