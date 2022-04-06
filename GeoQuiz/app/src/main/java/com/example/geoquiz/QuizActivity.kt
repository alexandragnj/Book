package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelProviders.of
import androidx.lifecycle.ViewModelStores.of
import java.util.EnumSet.of
import java.util.List.of
import java.util.Map.of
import java.util.Optional.of

private const val TAG="MainActivity"
private const val QUESTION_INDEX_KEY="index"
private const val QUESTIONS_ANSWERED_KEY="answer"

class QuizActivity : AppCompatActivity() {



    private lateinit var mTrueButton: Button
    private lateinit var mFalseButton: Button
    private lateinit var mNextButton: ImageButton
    private lateinit var mPrevButton: ImageButton
    private lateinit var mQuestionTextView: TextView
    private lateinit var scoreButton: Button


    private val quizViewModel: QuizViewModel by lazy{
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }


    private var clicked:BooleanArray= BooleanArray(4)
    private lateinit var correctAnswers: ArrayList<Int>
    private var score: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"onCreate(Bundle?) called")
        setContentView(R.layout.activity_quiz)

       /* val provider: ViewModelProvider=ViewModelProviders.of(this)
        val quizViewModel=provider.get(QuizViewModel::class.java)
        Log.d(TAG,"Got a QuizViewModel: $quizViewModel")*/

        mTrueButton=findViewById(R.id.true_button)
        mFalseButton=findViewById(R.id.false_button)
        mQuestionTextView=findViewById(R.id.question_text_view)
        mNextButton=findViewById(R.id.next_button)
        mPrevButton=findViewById(R.id.prev_button)
        scoreButton=findViewById(R.id.score_button)

        if(savedInstanceState!=null){
            quizViewModel.mCurrentIndex=savedInstanceState.getInt(QUESTION_INDEX_KEY)
            clicked= savedInstanceState.getBooleanArray(QUESTIONS_ANSWERED_KEY)!!
        }



       /* val question=mQuestionBank[mCurrentIndex].textResId
        mQuestionTextView.setText(question)
        Log.e("Activity","este: ${question.toString()}")*/

        mQuestionTextView.setOnClickListener {
            quizViewModel.moveToNext()
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
            quizViewModel.moveToNext()
            /*val question=mQuestionBank[mCurrentIndex].textResId
            mQuestionTextView.setText(question)*/

            updateQuestion()


        }

        mPrevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()

        }

        updateQuestion()

        scoreButton.setOnClickListener {

            for( i in correctAnswers){
                score += 1;
            }


            Toast.makeText(this,"Your score is: ${(score.toDouble()/4)*100}%",Toast.LENGTH_SHORT).show()

        }





    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart() called")
    }

    override fun onResume(){
        super.onResume()
        Log.d(TAG,"onResume() called")

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause() called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState() called")
        outState.putInt(QUESTION_INDEX_KEY,quizViewModel.mCurrentIndex)
        outState.putBooleanArray(QUESTIONS_ANSWERED_KEY,clicked)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy() called")
    }



    private fun updateQuestion(){
        val questionTextResId=quizViewModel.currentQuestionText
        mQuestionTextView.setText(questionTextResId)
        mFalseButton.isClickable= !(clicked[quizViewModel.mCurrentIndex]!!)
        mTrueButton.isClickable= !(clicked[quizViewModel.mCurrentIndex]!!)

    }

    private fun checkAnswer(userPressedTrue: Boolean){
        correctAnswers=ArrayList()
        val answerIsTrue=quizViewModel.currentQuestionAnswer
        var messageResId=0;
        if(userPressedTrue==answerIsTrue){
            messageResId=R.string.correct_toast
            correctAnswers.add(messageResId)

        }else{
            messageResId=R.string.incorrect_toast
        }

        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show()
        clicked[quizViewModel.mCurrentIndex]=true
        mTrueButton.isClickable=false
        mFalseButton.isClickable=false
    }


}