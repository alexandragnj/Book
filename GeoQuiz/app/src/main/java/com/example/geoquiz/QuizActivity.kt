package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders


private const val TAG = "MainActivity"
private const val QUESTION_INDEX_KEY = "index"
private const val QUESTIONS_ANSWERED_KEY = "answer"
private const val REQUEST_CODE_CHEAT = 0

class QuizActivity : AppCompatActivity() {


    private lateinit var mTrueButton: Button
    private lateinit var mFalseButton: Button
    private lateinit var mNextButton: ImageButton
    private lateinit var mPrevButton: ImageButton
    private lateinit var mQuestionTextView: TextView
    private lateinit var cheatButton: Button
    private lateinit var scoreButton: Button


    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }


    private var questionWasAnswered: BooleanArray = BooleanArray(4) //button true or false was clicked
    private lateinit var correctAnswers: ArrayList<Int>
    private var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_quiz)



        mTrueButton = findViewById(R.id.true_button)
        mFalseButton = findViewById(R.id.false_button)
        mQuestionTextView = findViewById(R.id.question_text_view)
        mNextButton = findViewById(R.id.next_button)
        mPrevButton = findViewById(R.id.prev_button)
        scoreButton = findViewById(R.id.score_button)
        cheatButton = findViewById(R.id.cheat_button)

        if (savedInstanceState != null) {
            quizViewModel.mCurrentIndex = savedInstanceState.getInt(QUESTION_INDEX_KEY)
            questionWasAnswered = savedInstanceState.getBooleanArray(QUESTIONS_ANSWERED_KEY)!!
        }



        mQuestionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        mTrueButton.setOnClickListener {

            checkAnswer(true)

        }

        mFalseButton.setOnClickListener {

            checkAnswer(false)

        }


        mNextButton.setOnClickListener {
            quizViewModel.moveToNext()

            updateQuestion()


        }

        mPrevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()

        }

        updateQuestion()

        scoreButton.setOnClickListener {

            for (i in correctAnswers) {
                score += 1
            }


            Toast.makeText(
                this,
                "Your score is: ${(score.toDouble() / quizViewModel.mQuestionBank.size) * 100}%",
                Toast.LENGTH_SHORT
            ).show()

        }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@QuizActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState() called")
        outState.putInt(QUESTION_INDEX_KEY, quizViewModel.mCurrentIndex)
        outState.putBooleanArray(QUESTIONS_ANSWERED_KEY, questionWasAnswered)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }


    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        mQuestionTextView.setText(questionTextResId)
        mFalseButton.isClickable = !(questionWasAnswered[quizViewModel.mCurrentIndex]!!)
        mTrueButton.isClickable = !(questionWasAnswered[quizViewModel.mCurrentIndex]!!)

    }

    private fun checkAnswer(userAnswer: Boolean) {
        correctAnswers = ArrayList()
        val answerIsTrue = quizViewModel.currentQuestionAnswer


        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == answerIsTrue -> R.string.correct_toast

            else -> R.string.incorrect_toast
        }


        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        questionWasAnswered[quizViewModel.mCurrentIndex] = true
        mTrueButton.isClickable = false
        mFalseButton.isClickable = false
    }


}