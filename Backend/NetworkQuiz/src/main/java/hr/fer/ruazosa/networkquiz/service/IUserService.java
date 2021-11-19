package hr.fer.ruazosa.networkquiz.service;

import hr.fer.ruazosa.networkquiz.model.User;

import java.util.List;

public interface IUserService {
    User registerUser(User user);
    boolean checkUsernameUnique(User user);
    User loginUser(User user);
    User getUserStats(String username);
    String getUserToken(String username);
    List<User> getAllOpponents(String usernameToExclude);
    List<User> getAllUsers();
    Integer setNewToken(String username, String token);
    List<User> getLeaderboard();
    Integer updateScoreAndCorrect(Long user_id, int score, int correct);

}
