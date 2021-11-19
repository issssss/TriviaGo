package hr.fer.ruazosa.networkquiz.controller;

import hr.fer.ruazosa.networkquiz.service.IGameService;
import hr.fer.ruazosa.networkquiz.model.Game;
import hr.fer.ruazosa.networkquiz.model.User;
import net.bytebuddy.implementation.bind.annotation.FieldValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GameController {

    @Autowired
    private IGameService gameService;

    @PostMapping("/createNewGame/{user}")
    public boolean createNewGame(@RequestBody Game game, @PathVariable("user") String username) {
        return gameService.createNewGame(game, username);
    }

    @PatchMapping("/joinGame/{id}")
    public boolean joinGameResponse(@PathVariable("id") Long gameId, @FieldValue("response") boolean response, @RequestParam("user_id") Long userId){
        if(!response){
            gameService.removeFromGame(gameId, userId);
        }
        Integer successCount = gameService.updatePending(gameId);
        return successCount > 0;
    }

    @GetMapping("/getPlayers/{id}")
    public List<User> getPlayers(@PathVariable("id") Long gameId){
        return gameService.getPlayers(gameId);
    }

    @PostMapping("/postResult/{id}")
    public boolean postResult(@PathVariable("id") Long gameId, @FieldValue("correct") int correct, @RequestParam("user_id") Long userId, @RequestParam("score") int score){
        return gameService.postResult(gameId, correct, score, userId);
    }

}
