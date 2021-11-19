package hr.fer.ruazosa.networkquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.math.MathUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import hr.fer.ruazosa.networkquiz.entity.CatQuestions
import hr.fer.ruazosa.networkquiz.entity.Game
import hr.fer.ruazosa.networkquiz.entity.Question
import hr.fer.ruazosa.networkquiz.entity.RunnableGame
import kotlin.math.floor

class GameActivity : AppCompatActivity() ,QuestionFragment.onDataPass{

    var fragmentContainer : FrameLayout?=null
    var remainingMinutes : TextView?=null
    var remainingSeconds : TextView?=null

    var questions: List<Question>?=null
    var gamedata: RunnableGame? = null
    var gameId: String? = null
    var current:Int = 0
    var correctlyAnswered : Int = 0
    var timer : CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //Getting the questions
        gamedata = intent.getSerializableExtra("gamedata") as RunnableGame
        gameId = intent.getSerializableExtra("game_id") as String
        questions = gamedata?.questions

        //Setup
        fragmentContainer = findViewById(R.id.fragmentContainer)
        remainingMinutes = findViewById(R.id.remainingMinutes)
        remainingMinutes?.text = "00"
        remainingSeconds = findViewById(R.id.remainingSeconds)

        // Timer setup
        timer = object: CountDownTimer(60000,1000){
            override fun onFinish() {
                finishGame()
            }

            override fun onTick(millisUntilFinished: Long) {
                remainingSeconds?.text = floor((millisUntilFinished/1000).toDouble()).toInt().toString()
            }
        }
        timer?.start()

        // Opens the first question
        openFragment(questions!![current].question)
    }



    // Evaluates the answer, shows the appropriate message, opens the next question
    override fun onDataPass(answer: String?) {
        val timeRemaining : Int = remainingSeconds?.text?.toString()?.toInt()!!
        if (answer != null) {
            if(answer.toLowerCase()?.equals(questions!![current].answer.toLowerCase())!!){
                val toastMsg = "Correct!"
                correctlyAnswered++
                Toast.makeText(this,toastMsg,Toast.LENGTH_SHORT).show()
            }
        }
        current++
        if (current < questions?.size!!)
            openFragment(questions!![current].question)
        //else if(timeRemaining < 2){}
        else{
            timer?.cancel()
            finishGame()
        }
    }

    // Skips, shows the msg, opens next question fragment
    override fun skipAnswer() {
        val timeRemaining : Int = remainingSeconds?.text?.toString()?.toInt()!!
        Toast.makeText(applicationContext, "Skipped!", Toast.LENGTH_SHORT).show()
        current++
        if (current < questions?.size!!)
            openFragment(questions!![current].question)
        //else if(timeRemaining < 2){}
        else{
            timer?.cancel()
            finishGame()
        }
    }

    // Opens the fragment, passing the textQuestion as the only parameter
    fun openFragment(questionText: String){
        var fragment : QuestionFragment = QuestionFragment.newInstance(questionText)
        var manager: FragmentManager = supportFragmentManager
        var transaction: FragmentTransaction = manager.beginTransaction()
        transaction.add(R.id.fragmentContainer,fragment,"QUESTION_FRAGMENT").commit()
    }

    fun finishGame(){
        Log.w("Pozvano", " finishGame()")
        val timeRemaining : Int = remainingSeconds?.text?.toString()?.toInt()!!
        var score : Long = correctlyAnswered.toLong()
        if(timeRemaining > 0){
            score = (timeRemaining * correctlyAnswered).toLong()
        }

        // Pokreni waiting for finish
        val intent = Intent(this, WaitForFinishActivity::class.java)
        intent.putExtra("game_id", gameId)
        intent.putExtra("score", score)
        intent.putExtra("correct", correctlyAnswered.toLong())
        startActivity(intent)
        finish()
    }

    public override fun onBackPressed() {
    }
}