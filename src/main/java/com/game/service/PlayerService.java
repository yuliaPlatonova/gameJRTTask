package com.game.service;


import com.game.entity.Player;
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

}
