package com.outdoorclassroom;

/**
 * Created by Allan on 03-Jun-17.
 */

public class QuestionAnswer {
    private String question;
    private String answer;
    private String imgName;


    private String nav;

    public QuestionAnswer() {
    }

    public QuestionAnswer(String q, String a, String i, String n) {
        this.question = q;
        this.answer = a;
        this.imgName = i;
        this.nav = n;
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

    public String getNav() {
        return nav;
    }
}
