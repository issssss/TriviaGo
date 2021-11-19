package hr.fer.ruazosa.networkquiz.model;

import javax.persistence.*;

@Entity
@Table(name = "game_users")
@IdClass(GameUsersId.class)
public class GameUsers {
    @Id
    @ManyToOne
    @JoinColumn(name = "gameId")
    private Game game;

    @Id
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "score")
    private int score;

    public void setGame(Game game){
        this.game = game;
    }
    public Game getGame(){
        return this.game;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getScore(){
        return this.score;
    }

}


