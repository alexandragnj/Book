package com.example.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG="MainActivity"

class QuizViewModel : ViewModel() {

     var mCurrentIndex: Int=0

     var mQuestionBank= listOf<Question>(
        Question(R.string.question_australia,true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),

        )

    val currentQuestionAnswer: Boolean
    get()=mQuestionBank[mCurrentIndex].answerTrue

    val currentQuestionText: Int
    get()=mQuestionBank[mCurrentIndex].textResId

    fun moveToNext(){
        mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.size
    }

    fun moveToPrev(){
        mCurrentIndex=(mCurrentIndex-1)%mQuestionBank.size
        if(mCurrentIndex<0){
            mCurrentIndex=mQuestionBank.size-1
        }
    }

   /* init{
        Log.d(TAG, "ViewModel instance created")

    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG,"ViewModel instance about to be destroyed")
    }*/
}