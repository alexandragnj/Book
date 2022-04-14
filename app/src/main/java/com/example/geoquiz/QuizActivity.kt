package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val QUESTION_INDEX_KEY = "index"
private const val QUESTIONS_ANSWERED_KEY = "answer"
private const val REQUEST_CODE_CHEAT = 0
private const val NUMBER_FOR_PERCENTAGE_CALCULATION = 100

class QuizActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button
    private lateinit var scoreButton: Button

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    private var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_quiz)

        bindViews()

        restoreState(savedInstanceState)

        setClickListeners()

        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            Log.d(TAG, "There is no result - onActiviyResult")
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
        outState.putInt(QUESTION_INDEX_KEY, quizViewModel.currentIndex)
        outState.putBooleanArray(QUESTIONS_ANSWERED_KEY, quizViewModel.questionWasAnswered)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            quizViewModel.currentIndex = savedInstanceState.getInt(QUESTION_INDEX_KEY)
            quizViewModel.questionWasAnswered =
                savedInstanceState.getBooleanArray(QUESTIONS_ANSWERED_KEY)!!
        }
    }

    private fun bindViews() {
        trueButton = findViewById(R.id._true)
        falseButton = findViewById(R.id._false)
        questionTextView = findViewById(R.id.question_text_view)
        nextButton = findViewById(R.id.next)
        prevButton = findViewById(R.id.previous)
        scoreButton = findViewById(R.id.score_button)
        cheatButton = findViewById(R.id.cheat)
    }

    private fun setClickListeners() {
        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        trueButton.setOnClickListener {
            quizViewModel.checkAnswer(true)
            Toast.makeText(this, quizViewModel.messageResId, Toast.LENGTH_SHORT).show()
            setAnswerButtonsClickable(false)
        }

        falseButton.setOnClickListener {
            quizViewModel.checkAnswer(false)
            Toast.makeText(this, quizViewModel.messageResId, Toast.LENGTH_SHORT).show()
            setAnswerButtonsClickable(false)
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()

            updateQuestion()
        }

        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        scoreButton.setOnClickListener {
            score = quizViewModel.correctAnswers

            val scoreFinalValue =
                (score.toDouble() / quizViewModel.questionBank.size) * NUMBER_FOR_PERCENTAGE_CALCULATION

            Toast.makeText(
                this,
                "Your score is: $scoreFinalValue%",
                Toast.LENGTH_SHORT
            ).show()
        }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@QuizActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        falseButton.isClickable = !(quizViewModel.questionWasAnswered[quizViewModel.currentIndex]!!)
        trueButton.isClickable = !(quizViewModel.questionWasAnswered[quizViewModel.currentIndex]!!)
    }

    private fun setAnswerButtonsClickable(clickableStatus: Boolean) {
        trueButton.isClickable = clickableStatus
        falseButton.isClickable = clickableStatus
    }
}
