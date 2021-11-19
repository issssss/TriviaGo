package hr.fer.ruazosa.networkquiz.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hr.fer.ruazosa.networkquiz.model.Question;
import hr.fer.ruazosa.networkquiz.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService implements IQuestionService{

    @Autowired
    private QuestionRepository questionRepository;

    
    @Override
    public List<Question> getQuestionsByGameId(Long gameId) {
        return questionRepository.getQuestionsByGameId(gameId);
    }
}
