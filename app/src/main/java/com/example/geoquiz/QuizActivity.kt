package com.example.geoquiz

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
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

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult(REQUEST_CODE_CHEAT, result)
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

    private fun onActivityResult(requestCode: Int, result: ActivityResult) {
        if (result.resultCode != Activity.RESULT_OK) {
            Log.d(TAG, "There is no result - onActiviyResult")
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            val intent = result.data
            quizViewModel.isCheater = intent?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
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
            setAnswerButtonsClickable()
        }

        falseButton.setOnClickListener {
            quizViewModel.checkAnswer(false)
            Toast.makeText(this, quizViewModel.messageResId, Toast.LENGTH_SHORT).show()
            setAnswerButtonsClickable()
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

        cheatButton.setOnClickListener { view ->
            openCheatActivity(view)
        }
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        falseButton.isClickable = !(quizViewModel.questionWasAnswered[quizViewModel.currentIndex])
        trueButton.isClickable = !(quizViewModel.questionWasAnswered[quizViewModel.currentIndex])
    }

    private fun setAnswerButtonsClickable() {
        trueButton.isClickable = false
        falseButton.isClickable = false
    }

    private fun openCheatActivity(view: View) {
        val answerIsTrue = quizViewModel.currentQuestionAnswer
        val intent = CheatActivity.newIntent(this@QuizActivity, answerIsTrue)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val options = ActivityOptionsCompat
                .makeClipRevealAnimation(view, 0, 0, view.width, view.height)
            resultLauncher.launch(intent, options)
        } else {
            resultLauncher.launch(intent)
        }
    }
}
