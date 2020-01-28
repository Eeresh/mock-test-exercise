package com.quiz;


import com.quiz.util.CommonUtil;
import com.quiz.util.QuizUtil;
import org.json.simple.parser.JSONParser;

public class QuizApplication {
    public static void main(String[] args) {
        QuizUtil quizUtil = new QuizUtil(new JSONParser(),new CommonUtil());
        quizUtil.runQuiz();
    }
}