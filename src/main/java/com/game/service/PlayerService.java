package com.game.service;


import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface PlayerService {
    List<Player> getAllPlayers(Specification<Player> specification);

    Page<Player> getAllPlayers(Specification<Player> specification, Pageable sortedPageable);

    Player createPlayer(Player requestPlayer);

    Player getPlayerById(Long id);

    Player updatePlayerById(Long id, Player player);

    void deletePlayerById(Long id);

    Long getIdFromString(String stringId);

    Specification<Player> filterByName(String name);

    Specification<Player> filterByTitle(String title);

    Specification<Player> filterByRace(Race race);

    Specification<Player> filterByProfession(Profession profession);

    Specification<Player> filterByExperience(Integer min, Integer max);

    Specification<Player> filterByLevel(Integer min, Integer max);

    Specification<Player> filterByBirthday(Long after, Long before);

    Specification<Player> filterByBan(Boolean isBanned);
}
