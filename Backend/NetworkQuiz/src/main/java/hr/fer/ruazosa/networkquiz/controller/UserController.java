package hr.fer.ruazosa.networkquiz.controller;

import hr.fer.ruazosa.networkquiz.service.IUserService;
import hr.fer.ruazosa.networkquiz.model.User;
import net.bytebuddy.implementation.bind.annotation.FieldValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/registerUser")
    public ResponseEntity<Object> registerUser(@RequestBody User user) {
        user.setCorrect(0);
        // validation
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        Map<String, Object> body = new LinkedHashMap<>();
        for (ConstraintViolation<User> violation : violations) {
            body.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        if (!body.isEmpty()) {
            return new ResponseEntity<Object>(body, HttpStatus.NOT_ACCEPTABLE);
        }
        //  check if user exists
        if (userService.checkUsernameUnique(user)) {
            userService.registerUser(user);
            return new ResponseEntity<Object>(user, HttpStatus.OK);
        }
        return new ResponseEntity<Object>(user, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/loginUser")
    public ResponseEntity<Object> loginUser(@RequestBody User user) {
        // validation
        if (user == null) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("error", "no user JSON object in body");
            return new ResponseEntity<Object>(body, HttpStatus.NOT_ACCEPTABLE);
        }
        else if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("error", "username or password parameters are empty");
            return new ResponseEntity<Object>(body, HttpStatus.NOT_ACCEPTABLE);
        }
        else {
            User loggedUser = userService.loginUser(user);
            if (loggedUser != null) {
                return new ResponseEntity<Object>(loggedUser, HttpStatus.OK);
            }
            else {
                Map<String, Object> body = new LinkedHashMap<>();
                body.put("error", "no user found");
                return new ResponseEntity<Object>(body, HttpStatus.NOT_FOUND);
            }
        }
    }

    @GetMapping("/userRank/{username}")
    public ResponseEntity<Object> getUserStats(@PathVariable String username){
        if (username == null) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("error", "no username in path");
            return new ResponseEntity<Object>(body, HttpStatus.NOT_ACCEPTABLE);
        }
        User user = userService.getUserStats(username);
        if (user != null) {
            return new ResponseEntity<Object>(user, HttpStatus.OK);
        }
        else {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("error", "no user found");
            return new ResponseEntity<Object>(body, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/usernames")
    public List<User> getAllUsernames(@RequestParam(name="usernameToExclude") String usernameToExclude){
       return userService.getAllOpponents(usernameToExclude);
    }

    @GetMapping("/token/{username}")
    public String getUserToken(@PathVariable String username){
        return userService.getUserToken(username);
    }

    @Modifying
    @PatchMapping("/token")
    public Integer setNewToken(@RequestParam String username, @FieldValue String token){
        return userService.setNewToken(username, token);
    }

    @GetMapping("/users")
    public List<User> getLeaderboard(){
        return userService.getLeaderboard();
    }

}
