package com.quiz.util;

import com.quiz.dto.AnswerDto;
import com.quiz.dto.QuestionDto;
import com.quiz.exception.FailedToReadFileException;
import com.quiz.exception.WrongAnswerException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class QuizUtilTest {
    private JSONParser jsonParser = Mockito.mock(JSONParser.class);
    private CommonUtil commonUtil = Mockito.mock(CommonUtil.class);
    private QuizUtil quizUtil = new QuizUtil(jsonParser,commonUtil);
    private List<QuestionDto> questionList = new ArrayList<>();

    @Before
    public void init() {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setQuestion("Which method can be used to check fileAccessiblity?");
        List<AnswerDto> answerDtos = new ArrayList<>();
        AnswerDto answerDto1 = new AnswerDto();
        answerDto1.setAnswer("isReadable(path), isWritable(path), and isExecutable(path)");
        answerDto1.setCorrect(true);
        answerDtos.add(answerDto1);
        questionDto.setAnswers(answerDtos);
        questionList.add(questionDto);
    }

    @Test
    public void testReadFile_Must_Fail_To_Read_File() throws IOException, ParseException {
        assertThatThrownBy(() -> quizUtil.readFile("Test.json"))
                .isInstanceOf(FailedToReadFileException.class);
    }

    @Test
    public void testReadFile_Must_Fail_While_Parsing() throws IOException, ParseException {
        when(jsonParser.parse(any(FileReader.class))).thenThrow(new ParseException(1));
        assertThatThrownBy(() -> quizUtil.readFile("QandA.json"))
                .isInstanceOf(FailedToReadFileException.class);
    }

    @Test
    public void testReadFile() throws IOException, ParseException {
        List<QuestionDto> expected = new ArrayList<>();
        when(jsonParser.parse(any(FileReader.class))).then(invocationOnMock -> {
            QuestionDto questionDto = new QuestionDto();
            questionDto.setQuestion("Testquestion");
            expected.add(questionDto);
            JSONArray array = new JSONArray();
            expected.forEach(gi -> {
                JSONObject obj = new JSONObject();
                obj.put("question", gi.getQuestion());
                obj.put("answers", gi.getAnswers());
                array.add(obj);
            });
            return array;
        });
        List<QuestionDto> actual = quizUtil.readFile("QandA.json");
        assertSame(expected.size(),actual.size());
    }

    @Test
    public void testPerformQuiz(){
        when(commonUtil.getOption()).thenReturn('a');
        quizUtil.performQuiz(questionList);
    }

    @Test
    public void testPerformQuiz_Must_Fail_For_Wrong_Answer(){
        when(commonUtil.getOption()).thenReturn('b');
        assertThatThrownBy(() -> quizUtil.performQuiz(questionList))
                .isInstanceOf(WrongAnswerException.class);
    }
}
