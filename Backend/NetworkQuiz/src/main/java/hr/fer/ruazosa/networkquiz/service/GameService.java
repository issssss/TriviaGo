package hr.fer.ruazosa.networkquiz.service;

import com.google.firebase.messaging.*;
import hr.fer.ruazosa.networkquiz.model.GameUsers;
import hr.fer.ruazosa.networkquiz.repository.GameRepository;
import hr.fer.ruazosa.networkquiz.model.Game;
import hr.fer.ruazosa.networkquiz.model.Question;
import hr.fer.ruazosa.networkquiz.model.User;
import hr.fer.ruazosa.networkquiz.repository.GameUsersRepository;
import hr.fer.ruazosa.networkquiz.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import sun.security.ssl.Debug;

//import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameService implements IGameService {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameUsersRepository gameUsersRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public int calculateScore(List<Question> questions, List<String> answers, int timeRemaining) {
        return 0;
    }

    @Override
    public int notifyGameStart(List<User> players, Long gameId) {
        List<String> tokens = new ArrayList<String>(){};

        for(User player : players){
            tokens.add(player.getToken());
        }
        String action;
        if(players.size() > 1) action = "begin";
        else action = "stop";
        MulticastMessage message = MulticastMessage.builder()
                .putData("action", action)
                .putData("game_id", String.valueOf(gameId))
                .addAllTokens(tokens)
                .build();

        try{
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            return response.getSuccessCount();
        }
        catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return 0;

    }

    @Override
    public Game startGame(int gameId) {
        return null;
    }

    @Override
    public Integer sendWinner(Game game, User user, int score) {
        List<String> playerTokens = new ArrayList<>();
        for(User player : game.getPlayers()){
            playerTokens.add(player.getToken());

        }
        MulticastMessage message;
        if(score > 0) {
            message = MulticastMessage.builder()
                    .putData("message", " The winner is " + user.getUsername())
                    .putData("username", user.getUsername())
                    .putData("score", String.valueOf(score))
                    .putData("action", "winner")
                    .addAllTokens(playerTokens)
                    .build();
        } else{
            message = MulticastMessage.builder()
                    .putData("message", " There is no winner")
                    .putData("username", "It's a tie!")
                    .putData("score", String.valueOf(score))
                    .putData("action", "winner")
                    .addAllTokens(playerTokens)
                    .build();
        }
        try{
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            return response.getSuccessCount();
        }
        catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return 0;

    }

    @Override
    @Transactional
    public Integer updatePending(Long gameId) {
        Integer successCount = gameRepository.updatePending(gameId);
        Game newGame = gameRepository.getGame(gameId);
        if(newGame.getPending() == 0){
            notifyGameStart(newGame.getPlayers(), gameId);
        }
        return successCount;
    }

    @Override
    @Transactional
    public Integer removeFromGame(Long gameId, Long userId) {
        return gameRepository.removeFromGame(gameId, userId);
    }

    @Override
    @Transactional
    public boolean createNewGame(Game game, String username) {
        for(Question question : game.getQuestions()){
            question.setGame(game);
        }
        Game newGame = gameRepository.save(game);
        List<String> playerTokens = new ArrayList<>();
        for(User player : newGame.getPlayers()){
            if(!player.getUsername().equals(username)){
                playerTokens.add(player.getToken());
            }
        }
        int sent = sendGameInvitations(playerTokens, username, newGame.getGameId());
        if(sent != playerTokens.size()){
            int notSent = playerTokens.size() - sent;
            //Debug.println("ERROR", notSent + " tokens not sent!");
        }
        //else Debug.println("OK", "all notifications sent");
        return true;
    }

    @Override
    public int sendGameInvitations(List<String> token, String username, Long gameId){
        MulticastMessage message = MulticastMessage.builder()
                .putData("message", username + " invited you to join a game")
                .putData("game_id", String.valueOf(gameId))
                .putData("action", "join")
                .addAllTokens(token)
                .build();
        try{
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            return response.getSuccessCount();
        }
        catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<User> getPlayers(Long gameId) {
        return gameRepository.getGame(gameId).getPlayers();
    }

    @Override
    @Transactional
    public boolean postResult(Long gameId, int correct, int score, Long userId) {

        GameUsers gameUsers = new GameUsers();
        gameUsers.setGame(gameRepository.getGame(gameId));
        gameUsers.setUser(userRepository.getUser(userId));
        gameUsers.setScore(score);
        gameUsersRepository.save(gameUsers);

        Integer success = updateFinished(gameId);
        userRepository.updateScoreAndCorrect(userId, score, correct);
        int finished = getFinished(gameId);
        List<User> players = getPlayers(gameId);
        if(finished == players.size()){
            Long winnerId = getWinner(gameId);
            User winner = userRepository.getUser(winnerId);
            score = winner.getScore();
            Game game = gameRepository.getGame(gameId);
            Integer t = sendWinner(game, winner, score);
            return true;
        }
        return false;
    }

    @Override
    public Long getWinner(Long gameId) {
        return gameUsersRepository.getWinner(gameId);//gameUsersRepository.getWinner(gameId);
    }

    @Override
    @Transactional
    public Integer updateFinished(Long gameId) {
        return gameRepository.updateFinished(gameId);
    }

    @Override
    public int getFinished(Long gameId) {
        return gameRepository.getFinished(gameId);
    }

}
