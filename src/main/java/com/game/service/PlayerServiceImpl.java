package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PlayerServiceImpl implements PlayerService{
    private PlayerRepository repository;

    @Autowired
    public void setPlayerRepository(PlayerRepository repository){
        this.repository = repository;
    }

    @Override
    public List<Player> getAllPlayers(Specification<Player> specification) {
        return repository.findAll(specification);
    }

    @Override
    public Page<Player> getAllPlayers(Specification<Player> specification, Pageable sortedPageable) {
        return repository.findAll(specification, sortedPageable);
    }

    //need to redo
    @Override
    public Player createPlayer(Player requestPlayer) {
        return repository.saveAndFlush(requestPlayer);
    }

    //need to add exception case
    @Override
    public Player getPlayerById(Long id) {
        return repository.findById(id).get();
    }

    //need to do
    @Override
    public Player updatePlayerById(Long id, Player player) {
        return null;
    }

    //need to add exception case
    @Override
    public void deletePlayerById(Long id) {
        repository.deleteById(id);
    }
}
