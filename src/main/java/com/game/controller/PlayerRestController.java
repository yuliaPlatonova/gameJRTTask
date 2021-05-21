package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class PlayerRestController {
    private PlayerService service;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.service = playerService;
    }

//need to do
    @GetMapping("/players")
    @ResponseStatus(HttpStatus.OK)
    public List<Player> getAllPlayersList() {
        return null;
    }

    //need to do
    @GetMapping("/players/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer getPlayersCount(){
        return null;
    }

    @PostMapping("/players")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player createPlayer(@RequestBody Player player){
        return service.createPlayer(player);
    }

    @GetMapping("/players/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player getPlayerById(@PathVariable(value = "id") String id){
        Long longId = service.getIdFromString(id);
        return service.getPlayerById(longId);
    }

    @PostMapping("/players/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player updatePlayer(@PathVariable(value = "id") String id, @RequestBody Player player){
        Long longId = service.getIdFromString(id);
        return service.updatePlayerById(longId, player);
    }

    @DeleteMapping("/players/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deletePlayerById(@PathVariable(value = "id") String id){
        Long longId = service.getIdFromString(id);
        service.deletePlayerById(longId);
    }
}
