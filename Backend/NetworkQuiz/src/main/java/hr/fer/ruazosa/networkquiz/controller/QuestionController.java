package hr.fer.ruazosa.networkquiz.controller;

import hr.fer.ruazosa.networkquiz.model.Question;
import hr.fer.ruazosa.networkquiz.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QuestionController {

    @Autowired
    private IQuestionService questionService;


    @GetMapping("/getQuestions/{id}")
    public List<Question> getQuestionsByGameId(@PathVariable("id") Long gameId){
        return questionService.getQuestionsByGameId(gameId);
    }
}
