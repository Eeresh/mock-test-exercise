package com.quiz.util;

import java.text.DecimalFormat;
import java.util.Scanner;

public class CommonUtil {
    private final Scanner sc = new Scanner(System.in);

    public char getOption() {
        return sc.next().charAt(0);
    }

    public float calculatePercentage(Integer totalCorrectAnswer, Integer totalQuestion){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        if(totalCorrectAnswer != 0) {
            float score = ((float)totalCorrectAnswer/(float)totalQuestion);
            return Float.parseFloat(decimalFormat.format(score * 100));
        }else
            return 0;
    }

    public Scanner getScanner() {
        return sc;
    }
}
