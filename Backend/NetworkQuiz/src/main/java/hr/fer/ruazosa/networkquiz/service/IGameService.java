package hr.fer.ruazosa.networkquiz.service;

import hr.fer.ruazosa.networkquiz.model.Game;
import hr.fer.ruazosa.networkquiz.model.Question;
import hr.fer.ruazosa.networkquiz.model.User;

import java.util.List;

public interface IGameService {

    int calculateScore(List<Question> questions, List<String> answers, int timeRemaining); //tocni odgovori*broj preostalih sekunda
    int notifyGameStart(List<User> players, Long gameId);
    Game startGame(int gameId);
    Integer sendWinner(Game game, User user, int score);
    Integer updatePending(Long gameId);
    Integer removeFromGame(Long gameId, Long userId);
    boolean createNewGame(Game game, String username);
    int sendGameInvitations(List<String> token, String username, Long gameId);
    List<User> getPlayers(Long gameId);
    boolean postResult(Long gameId, int correct, int score, Long userId);
    Long getWinner(Long gameId);
    Integer updateFinished(Long gameId);
    int getFinished(Long gameId);
}
