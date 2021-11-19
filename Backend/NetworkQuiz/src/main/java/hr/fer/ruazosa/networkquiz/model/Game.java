package hr.fer.ruazosa.networkquiz.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gameId")
    private Long gameId;

    @Size(min=5, message = "Game must have at least 5 questions")
    @OneToMany(
            mappedBy = "game",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Question> questions = new ArrayList<>();

    @Size(min=2, message = "Game must have at least 2 players")
    @ManyToMany
    @JoinTable(
            name = "USER_GAMES",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> players = new ArrayList<>();

    @Column(name = "pending")
    private int pending;

    @Column(name = "finished")
    private int finished;

    public void setGameId(Long gameId){
        this.gameId = gameId;
    }

    public void setQuestions(List<Question> questions){
        this.questions = questions;
    }

    public void setPlayers(List<User> playerTokens){
        this.players = playerTokens;
    }

    public void setPending(int pending){
        this.pending = pending;
    }

    public void setFinished(int finished){
        this.finished = finished;
    }

    public Long getGameId(){
        return this.gameId;
    }

    public List<Question> getQuestions(){
        return this.questions;
    }

    public List<User> getPlayers(){
        return this.players;
    }

    public int getPending(){
        return this.pending;
    }

    public int getFinished(){
        return this.finished;
    }

    public void removePlayer(User token){
        this.players.remove(token);
    }

}
