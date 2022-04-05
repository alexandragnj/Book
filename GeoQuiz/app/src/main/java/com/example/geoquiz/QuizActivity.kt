package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class QuizActivity : AppCompatActivity() {

    private lateinit var mTrueButton: Button
    private lateinit var mFalseButton: Button
    private lateinit var mNextButton: ImageButton
    private lateinit var mPrevButton: ImageButton
    private lateinit var mQuestionTextView: TextView

    private var mCurrentIndex: Int=0

    private var mQuestionBank= arrayListOf<Question>(
        Question(R.string.question_australia,true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        mTrueButton=findViewById(R.id.true_button)
        mFalseButton=findViewById(R.id.false_button)
        mQuestionTextView=findViewById(R.id.question_text_view)
        mNextButton=findViewById(R.id.next_button)
        mPrevButton=findViewById(R.id.prev_button)



       /* val question=mQuestionBank[mCurrentIndex].textResId
        mQuestionTextView.setText(question)
        Log.e("Activity","este: ${question.toString()}")*/

        mQuestionTextView.setOnClickListener {
            mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.size
            updateQuestion()
        }

        mTrueButton.setOnClickListener {
            /*val toast=Toast.makeText(this, "Correct",Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP,0,20)
            toast.show()*/
            checkAnswer(true)
        }

        mFalseButton.setOnClickListener {
            checkAnswer(false)
        }

        mNextButton.setOnClickListener {
            mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.size
            /*val question=mQuestionBank[mCurrentIndex].textResId
            mQuestionTextView.setText(question)*/
            updateQuestion()
        }

        mPrevButton.setOnClickListener {
            mCurrentIndex=(mCurrentIndex-1)%mQuestionBank.size
            updateQuestion()
        }

        updateQuestion()
    }

    private fun updateQuestion(){
        val question=mQuestionBank[mCurrentIndex].textResId
        mQuestionTextView.setText(question)
    }

    private fun checkAnswer(userPressedTrue: Boolean){
        val answerIsTrue=mQuestionBank[mCurrentIndex].answerTrue
        var messageResId=0;
        if(userPressedTrue==answerIsTrue){
            messageResId=R.string.correct_toast
        }else{
            messageResId=R.string.incorrect_toast
        }

        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show()
    }


}