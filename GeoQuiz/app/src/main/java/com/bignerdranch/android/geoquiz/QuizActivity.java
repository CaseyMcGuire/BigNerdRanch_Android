package com.bignerdranch.android.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class QuizActivity extends AppCompatActivity {

  private static final String TAG = "QuizActivity";
  private static final String KEY_INDEX = "index";
  private static final String CHEATED_QUESTIONS = "cheatedQuestions";
  private static final int REQUEST_CODE_CHEAT = 0;


  private Button mTrueButton;
  private Button mFalseButton;
  private Button mNextButton;
  private Button mPreviousButton;
  private Button mCheatButton;
  private TextView mQuestionTextView;



  private Question[] mQuestionBank = new Question[] {
    new Question(R.string.question_australia, true),
    new Question(R.string.question_oceans, true),
    new Question(R.string.question_mideast, false),
    new Question(R.string.question_africa, false),
    new Question(R.string.question_americas, true),
    new Question(R.string.question_asia, true)
  };

  private boolean[] mCheatedQuestionIndices = new boolean[mQuestionBank.length];
  private int mCurrentIndex = 0;

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != RESULT_OK) {
      return;
    }

    if (requestCode == REQUEST_CODE_CHEAT) {
      if (data == null) {
        return;
      }
      mCheatedQuestionIndices[mCurrentIndex] = CheatActivity.wasAnswerShown(data);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // This method inflates a layout and puts it on screen. When a layout is inflated, each widget
    // in the layout is instantiated as defined by its attributes.
    setContentView(R.layout.activity_quiz);

    if (savedInstanceState != null) {
      mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
      mCheatedQuestionIndices = savedInstanceState.getBooleanArray(CHEATED_QUESTIONS);
    }

    mQuestionTextView = findViewById(R.id.question_text_view);

    mTrueButton = findViewById(R.id.true_button);
    mTrueButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        checkAnswer(true);
      }
    });

    mFalseButton = findViewById(R.id.false_button);
    mFalseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        checkAnswer(false);
      }
    });

    mNextButton = findViewById(R.id.next_button);
    mNextButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        updateQuestion();
      }
    });

    mPreviousButton = findViewById(R.id.previous_button);
    mPreviousButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mCurrentIndex--;
        if (mCurrentIndex < 0) {
          mCurrentIndex = mQuestionBank.length - 1;
        }
        updateQuestion();
      }
    });

    mCheatButton = findViewById(R.id.cheat_button);
    mCheatButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        boolean isAnswerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        Intent intent = CheatActivity.newIntent(QuizActivity.this, isAnswerTrue);
        startActivityForResult(intent, REQUEST_CODE_CHEAT);
      }
    });

    updateQuestion();
  }

  @Override
  protected void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    Log.i(TAG, "onSaveInstanceState");
    savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    savedInstanceState.putBooleanArray(CHEATED_QUESTIONS, mCheatedQuestionIndices);
  }

  private void updateQuestion() {
    int question = mQuestionBank[mCurrentIndex].getTextResId();
    mQuestionTextView.setText(question);
  }

  private void checkAnswer(boolean userPressedTrue) {
    boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

    int messageResId = 0;
    if (isCheater()) {
      messageResId = R.string.judgement_toast;
    } else {
      if (userPressedTrue == answerIsTrue) {
        messageResId = R.string.correct_toast;
      } else {
        messageResId = R.string.incorrect_toast;
      }
    }
    Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
  }

  private boolean isCheater() {
    return mCheatedQuestionIndices[mCurrentIndex];
  }
}
