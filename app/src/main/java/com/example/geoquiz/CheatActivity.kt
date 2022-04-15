package com.example.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders

private const val EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.example.geoquiz.answer_shown"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    private var answerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        bindView()

        setClickListeners()

        if (quizViewModel.showAnswerClicked) {
            answerTextView.isVisible = true
            if (answerIsTrue) {
                answerTextView.setText(R.string._true)
                setAnswerShownResult(true)
            } else {
                answerTextView.setText(R.string._false)
                setAnswerShownResult(true)
            }
        }
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    private fun bindView() {
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer)
    }

    private fun setClickListeners() {
        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string._true
                else -> R.string._false
            }

            answerTextView.isVisible = true
            answerTextView.setText(answerText)
            setAnswerShownResult(true)
            quizViewModel.showAnswerClicked = true
        }
    }
}
