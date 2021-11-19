package hr.fer.ruazosa.networkquiz.repository;

import hr.fer.ruazosa.networkquiz.model.Game;
import hr.fer.ruazosa.networkquiz.model.GameUsers;
import hr.fer.ruazosa.networkquiz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameUsersRepository extends JpaRepository<GameUsers, Integer> {

    @Query(value = "SELECT TOP 1 user_Id FROM Game_Users WHERE game_Id = ?1 ORDER BY score DESC", nativeQuery = true)
    Long getWinner(Long gameId);

}
