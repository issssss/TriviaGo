package hr.fer.ruazosa.networkquiz.repository;

import hr.fer.ruazosa.networkquiz.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer>{

    @Query(value="SELECT * FROM QUESTION WHERE GAME_ID = ?1",nativeQuery = true)
    public List<Question> getQuestionsByGameId(Long game_id);
}