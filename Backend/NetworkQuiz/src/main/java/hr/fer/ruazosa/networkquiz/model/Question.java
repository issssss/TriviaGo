package hr.fer.ruazosa.networkquiz.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name="question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "question_id")
    private Long id;

    //@NotBlank(message = "Question text cannot be empty")
    @Column(name = "question_text")
    private String question;

    //@NotBlank(message = "Question answer cannot be empty")
    @Column(name = "question_answer")
    private String answer;

    /*
    @Column(name = "my_game_id")
    private Long my_game_id;

     */

    // Bez ovoga se dobije beskonacna rekurzija u JSON-u
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "gameId")
    private Game game;

    public void setId(Long id){
        this.id = id;
    }

    public void setQuestion(String question){
        this.question = question;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }

    public void setGame(Game game){
        this.game = game;
    }


    public Long getId(){
        return this.id;
    }

    public String getQuestion(){
        return this.question;
    }

    public String getAnswer(){
        return this.answer;
    }

    public Game getGame(){
        return this.game;
    }
}
