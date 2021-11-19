package hr.fer.ruazosa.networkquiz.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import hr.fer.ruazosa.networkquiz.repository.UserRepository;
import hr.fer.ruazosa.networkquiz.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean checkUsernameUnique(User user) {
        List userList = userRepository.findByUserName(user.getUsername());
        if (userList.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public User loginUser(User user) {
        List<User> loggedUserList = userRepository.findByUserNameAndPassword(user.getUsername(), user.getPassword());
        if (loggedUserList.isEmpty()) {
            return null;
        }
        return userRepository.findByUserNameAndPassword(user.getUsername(), user.getPassword()).get(0);
    }

    @Override
    public User getUserStats(String username) {
        return userRepository.getUserStats(username);
    }


    @Override
    public List<User> getAllOpponents(String usernameToExclude) { return userRepository.getAllOpponents(usernameToExclude); }

    @Override
    public List<User> getAllUsers() { return userRepository.getAllUsers(); }


    @Override
    @Transactional
    public Integer setNewToken(String username, String token) {
        return userRepository.setNewToken(username, token);
    }

    @Override
    public String getUserToken(String username){ return userRepository.getUserToken(username);}

    @Override
    public List<User> getLeaderboard() {
        List<User> allUsers = userRepository.getAllUsers();
        
        return allUsers;
    }

    @Override
    public Integer updateScoreAndCorrect(Long user_id, int score, int correct) {
        return userRepository.updateScoreAndCorrect(user_id,score,correct);
    }

}
