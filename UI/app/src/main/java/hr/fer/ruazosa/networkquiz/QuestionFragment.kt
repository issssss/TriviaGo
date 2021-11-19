package hr.fer.ruazosa.networkquiz

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView


private const val QUESTION_TEXT = "param1"


class QuestionFragment : Fragment() {
    private var mText: String? = null

    // Used to send the answer from the fragment to the activity ( see: Interface on bottom )
    lateinit var dataPasser: onDataPass

    private var questionTextView : TextView? = null
    private var answerTextField : EditText? = null
    private var skipButton : Button? = null
    private var confirmButton: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mText = it.getString(QUESTION_TEXT)
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val retval : View = inflater.inflate(R.layout.fragment_question, container, false)

        // Set the appropriate views
        questionTextView = retval.findViewById(R.id.questionTextView)
        answerTextField = retval.findViewById(R.id.questionAnswerEditText)
        skipButton = retval.findViewById(R.id.skipQuestionButton)
        confirmButton = retval.findViewById(R.id.confirmAnswerButton)

        // Set the question text
        questionTextView?.text = mText

        // On press, sends the answer back to the activity
        // Also destroys the fragment
        confirmButton?.setOnClickListener {
            val answer: String = answerTextField?.text.toString()
            sendAnswer(answer)
            fragmentManager?.popBackStack()
        }

        // On press, skips the answer
        // Also destroys the fragment
        skipButton?.setOnClickListener{
            skipAnswer()
            fragmentManager?.popBackStack()
        }
        return retval
    }

    // Once the fragment attaches, dataPasser is initialized
    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as onDataPass
    }

    // Sends the answer back to the activity
    fun sendAnswer (answer: String? ){
        dataPasser.onDataPass(answer)
    }
    // Notifies the activity that the answer was skipped
    fun skipAnswer (){
        dataPasser.skipAnswer()
    }

    companion object {
        @JvmStatic
        fun newInstance(questionText: String) =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                    putString(QUESTION_TEXT, questionText)
                }
            }
    }

    interface onDataPass{
        fun onDataPass(answer: String?)
        fun skipAnswer()
    }
}