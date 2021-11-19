package hr.fer.ruazosa.networkquiz.service;

import hr.fer.ruazosa.networkquiz.model.Question;

import java.util.List;

public interface IQuestionService {

    List<Question> getQuestionsByGameId(Long gameId);
}
