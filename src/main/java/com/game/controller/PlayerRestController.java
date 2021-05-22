package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public List<Player> getAllPlayersList(@RequestParam(value = "name", required = false) String name,
                                          @RequestParam(value = "title", required = false) String title,
                                          @RequestParam(value = "race", required = false) Race race,
                                          @RequestParam(value = "profession", required = false) Profession profession,
                                          @RequestParam(value = "after", required = false) Long after,
                                          @RequestParam(value = "before", required = false) Long before,
                                          @RequestParam(value = "banned", required = false) Boolean banned,
                                          @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                          @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                          @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                          @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                          @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
                                          @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        return service.getAllPlayers(Specification.where(service.filterByName(name))
                .and(service.filterByTitle(title))
                .and(service.filterByRace(race))
                .and(service.filterByProfession(profession))
                .and(service.filterByBirthday(after, before))
                .and(service.filterByBan(banned))
                .and(service.filterByExperience(minExperience, maxExperience))
                .and(service.filterByLevel(minLevel, maxLevel)), pageable).getContent();
    }

    //need to do
    @GetMapping("/players/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer getPlayersCount(@RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "title", required = false) String title,
                                   @RequestParam(value = "race", required = false) Race race,
                                   @RequestParam(value = "profession", required = false) Profession profession,
                                   @RequestParam(value = "after", required = false) Long after,
                                   @RequestParam(value = "before", required = false) Long before,
                                   @RequestParam(value = "banned", required = false) Boolean banned,
                                   @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                   @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                   @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                   @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {

        return service.getAllPlayers(Specification.where(service.filterByName(name))
                .and(service.filterByTitle(title))
                .and(service.filterByRace(race))
                .and(service.filterByProfession(profession))
                .and(service.filterByBirthday(after, before))
                .and(service.filterByBan(banned))
                .and(service.filterByExperience(minExperience, maxExperience))
                .and(service.filterByLevel(minLevel, maxLevel))).size();
    }

    @PostMapping("/players")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player createPlayer(@RequestBody Player player) {
        return service.createPlayer(player);
    }

    @GetMapping("/players/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player getPlayerById(@PathVariable(value = "id") String id) {
        Long longId = service.getIdFromString(id);
        return service.getPlayerById(longId);
    }

    @PostMapping("/players/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player updatePlayer(@PathVariable(value = "id") String id, @RequestBody Player player) {
        Long longId = service.getIdFromString(id);
        return service.updatePlayerById(longId, player);
    }

    @DeleteMapping("/players/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deletePlayerById(@PathVariable(value = "id") String id) {
        Long longId = service.getIdFromString(id);
        service.deletePlayerById(longId);
    }
}
