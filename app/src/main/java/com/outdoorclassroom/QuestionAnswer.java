package com.outdoorclassroom;

/**
 * Created by Allan on 03-Jun-17.
 */

public class QuestionAnswer {
    private String question, answer, imgName;

    public QuestionAnswer() {
    }

    public QuestionAnswer(String q, String a, String i) {
        this.question = q;
        this.answer = a;
        this.imgName = i;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getImgName() {
        return imgName;
    }
}
