package com.example.geoquiz

import android.widget.Toast
import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    var currentIndex: Int = 0
    var isCheater = false
    lateinit var correctAnswers: ArrayList<Int>
    var messageResId: Int = 0


    var questionBank = listOf<Question>(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),

        )

    var questionWasAnswered: BooleanArray = BooleanArray(questionBank.size)

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answerTrue

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = (currentIndex - 1) % questionBank.size
        if (currentIndex < 0) {
            currentIndex = questionBank.size - 1
        }
    }

    fun checkAnswer(userAnswer: Boolean) {
        correctAnswers = ArrayList()

        val answerIsTrue = currentQuestionAnswer

        messageResId = when {
            isCheater -> R.string.judgment_toast
            userAnswer == answerIsTrue -> {
                R.string.correct_toast
            }

            else -> R.string.incorrect_toast
        }
        if (userAnswer == answerIsTrue) {
            correctAnswers.add(messageResId)
        }

        questionWasAnswered[currentIndex] = true


    }
}
