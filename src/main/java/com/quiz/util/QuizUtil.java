package com.quiz.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.dto.AnswerDto;
import com.quiz.dto.QuestionDto;
import com.quiz.exception.FailedToReadFileException;
import com.quiz.exception.WrongAnswerException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class QuizUtil {
    private JSONParser jsonParser;
    private CommonUtil commonUtil;

    public QuizUtil(JSONParser jsonParser,CommonUtil commonUtil){
        this.jsonParser = jsonParser;
        this.commonUtil = commonUtil;
    }

    public List<QuestionDto> readFile(String fileName) {
        try (FileReader reader = new FileReader(fileName)) {
            JSONArray obj = (JSONArray) jsonParser.parse(reader);
            return Arrays.asList(new ObjectMapper().readValue(obj.toJSONString(), QuestionDto[].class));
        }catch (IOException | ParseException e){
           throw new FailedToReadFileException(e.getLocalizedMessage());
        }
    }

    public void performQuiz(List<QuestionDto> questionList) throws WrongAnswerException{
        Integer totalQuestion = questionList.size();
        Integer totalCorrectAnswer = 0;
        float percentage = 0;
        char[] order = {'a', 'b', 'c', 'd'};
        Map<Character, AnswerDto> answersMap = new HashMap<>();
        for (int i = 0; i <= questionList.size() - 1; i++) {
            QuestionDto question = questionList.get(i);
            System.out.println((i + 1) + ")" + question.getQuestion());
            List<AnswerDto> answers = question.getAnswers();
            Collections.shuffle(answers);
            for (int j = 0; j < answers.size(); j++) {
                answersMap.put(order[j], answers.get(j));
                System.out.println("  " + order[j] + ")" + answers.get(j).getAnswer());
            }
            System.out.print("Your Answer:");
            char a = commonUtil.getOption();
            if(answersMap.keySet().contains(a)) {
                AnswerDto answerDto = answersMap.get(a);
                if (!answerDto.isCorrect()) {
                    percentage = commonUtil.calculatePercentage(totalCorrectAnswer, totalQuestion);
                    throw new WrongAnswerException(question.getExplanation(), percentage);
                } else totalCorrectAnswer = totalCorrectAnswer + 1;
            }else{
                percentage = commonUtil.calculatePercentage(totalCorrectAnswer, totalQuestion);
                throw new WrongAnswerException(question.getExplanation(), percentage);
            }
        }
        System.out.println("Successfully Completed");
        System.out.println("Your percentage : " +commonUtil.calculatePercentage(totalCorrectAnswer,totalQuestion)+"%");
    }

    public void runQuiz() {
       try {
           List<QuestionDto> questionList = readFile("QandA.json");
           Collections.shuffle(questionList);
           System.out.println("Welcome to quiz application");
           System.out.println("Do you want to continue: Y/N");
           char c = commonUtil.getOption();
           switch (c) {
               case 'Y':
                   performQuiz(questionList);
                   break;
               case 'N':
               default:
                   System.out.println("Thank you");
           }
       }catch (WrongAnswerException e) {
            System.out.println("Correct answer is: " + e.getExplanation());
            System.out.println("Your Percentage: " + e.getScoredPercentage()+"%");
        }finally {
           commonUtil.getScanner().close();
       }
    }
}
