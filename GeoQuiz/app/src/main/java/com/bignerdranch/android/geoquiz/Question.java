package com.bignerdranch.android.geoquiz;


public class Question {
  private int mTextResId;
  private boolean mAnswerTrue;

  public Question(int textResId, boolean answerTrue) {
    mTextResId = textResId;
    mAnswerTrue = answerTrue;
  }

  public int getTextResId() {
    return mTextResId;
  }

  public void setTextResId(int TextResId) {
    this.mTextResId = TextResId;
  }

  public boolean isAnswerTrue() {
    return mAnswerTrue;
  }

  public void setAnswerTrue(boolean answerTrue) {
    this.mAnswerTrue = answerTrue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Question question = (Question) o;

    if (mTextResId != question.mTextResId) return false;
    return mAnswerTrue == question.mAnswerTrue;
  }

  @Override
  public int hashCode() {
    int result = mTextResId;
    result = 31 * result + (mAnswerTrue ? 1 : 0);
    return result;
  }
}
