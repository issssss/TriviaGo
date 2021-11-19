package hr.fer.ruazosa.networkquiz.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @NotBlank(message = "First name cannot be empty")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Column(name = "last_name")
    private String lastName;

    @Email(message = "Email not in correct format")
    @Column(name = "e_mail")
    private String email;

    @NotBlank(message = "username name cannot be empty")
    private String username;
    @NotBlank(message = "Password name cannot be empty")
    private String password;

    @NotBlank(message = "Token missing")
    @Column(name = "token")
    private String token;

    @Column(name = "score")
    private int score;
    @Column(name = "games_played")
    private int gamesPlayed;
    @Column(name = "correct")
    private int correct = 0;
    @Column(name = "rank")
    private int rank = 0;

    @ManyToMany(mappedBy = "players", cascade = CascadeType.REMOVE)
    private List<Game> games = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token){this.token = token;}

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken(){return token; }

    public void setScore(int score) {
        this.score = score;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public void setRank(int rank){
        this.rank = rank;
    }

    public int getScore() {
        return this.score;
    }

    public int getCorrect(){
        return this.correct;
    }

    public int getGamesPlayed(){
        return this.gamesPlayed;
    }

    public int getRank(){
        return this.rank;
    }


}
