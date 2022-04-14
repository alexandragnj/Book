package com.example.geoquiz

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    var currentIndex: Int = 0
    var isCheater = false
    var correctAnswers: Int = 0
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
        currentIndex = (currentIndex - 1)
        if (currentIndex < 0) {
            currentIndex = questionBank.size - 1
        }
    }

    fun checkAnswer(userAnswer: Boolean) {
        val answerIsTrue = currentQuestionAnswer

        messageResId = when {
            isCheater -> R.string.judgment_toast
            userAnswer == answerIsTrue -> {
                R.string.correct
            }

            else -> R.string.incorrect
        }
        if (userAnswer == answerIsTrue) {
            correctAnswers += 1
        }

        questionWasAnswered[currentIndex] = true
    }
}
