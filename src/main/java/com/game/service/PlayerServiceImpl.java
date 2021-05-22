package com.game.service;

import com.game.BadRequestException;
import com.game.PlayerNotFoundException;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {
    private PlayerRepository repository;

    @Autowired
    public void setPlayerRepository(PlayerRepository repository) {
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

    @Override
    public Player createPlayer(Player requestPlayer) {
        if (requestPlayer.getName() == null || requestPlayer.getTitle() == null ||
                requestPlayer.getRace() == null || requestPlayer.getProfession() == null ||
                requestPlayer.getBirthday() == null || requestPlayer.getExperience() == null) {
            throw new BadRequestException("One of player parameters is null");
        }

        playerValidation(requestPlayer);
        if (requestPlayer.isBanned() == null) {
            requestPlayer.setBanned(false);
        }
        requestPlayer.setLevel(getPlayerLevel(requestPlayer));
        requestPlayer.setUntilNextLevel(getExperienceUntilNextLevel(requestPlayer));

        return repository.saveAndFlush(requestPlayer);
    }

    private void playerValidation(Player player) {
        if (player.getName() != null && (player.getName().length() == 0 || player.getName().length() > 12)) {
            throw new BadRequestException("Player's name is not valid");
        }
        if (player.getTitle() != null && (player.getTitle().length() > 30)) {
            throw new BadRequestException("Player's title is not valid");
        }
        if (player.getBirthday() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(player.getBirthday());
            if (calendar.get(Calendar.YEAR) < 2000 || calendar.get(Calendar.YEAR) > 3000) {
                throw new BadRequestException("Player's birthday is not valid");
            }
        }
        if (player.getExperience() != null &&
                (player.getExperience() < 0 || player.getExperience() > 10_000_000)) {
            throw new BadRequestException("Player's experience is not valid");
        }
    }

    @Override
    public Player getPlayerById(Long id) {
        if (!repository.existsById(id)) {
            throw new PlayerNotFoundException("Player not found");
        }
        return repository.findById(id).get();
    }

    @Override
    public Player updatePlayerById(Long id, Player player) {
        playerValidation(player);
        if (!repository.existsById(id)) {
            throw new PlayerNotFoundException("Player not exist");
        }
        Player updatedPlayer = repository.findById(id).get();

        if (player.getName() != null) {
            updatedPlayer.setName(player.getName());
        }
        if (player.getTitle() != null) {
            updatedPlayer.setTitle(player.getTitle());
        }
        if (player.getRace() != null) {
            updatedPlayer.setRace(player.getRace());
        }
        if (player.getProfession() != null) {
            updatedPlayer.setProfession(player.getProfession());
        }
        if (player.getBirthday() != null) {
            updatedPlayer.setBirthday(player.getBirthday());
        }
        if (player.isBanned() != null) {
            updatedPlayer.setBanned(player.isBanned());
        }
        if (player.getExperience() != null) {
            updatedPlayer.setExperience(player.getExperience());
        }
        Integer level = getPlayerLevel(updatedPlayer);
        updatedPlayer.setLevel(level);
        Integer untilNextLevel = getExperienceUntilNextLevel(updatedPlayer);
        updatedPlayer.setUntilNextLevel(untilNextLevel);

        return repository.save(updatedPlayer);
    }

    @Override
    public void deletePlayerById(Long id) {
        if (!repository.existsById(id)) {
            throw new PlayerNotFoundException("Player not found");
        }
        repository.deleteById(id);
    }

    @Override
    public Long getIdFromString(String stringId) {
        if (stringId == null || stringId.equals("") || stringId.equals("0")) {
            throw new BadRequestException("Player not found");
        }
        try {
            return Long.parseLong(stringId);
        } catch (Exception exception) {
            throw new BadRequestException("Id is not valid");
        }
    }

    @Override
    public Specification<Player> filterByName(String name) {
        if (name == null) {
            return null;
        }
        return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
    }

    @Override
    public Specification<Player> filterByTitle(String title) {
        if (title == null) {
            return null;
        }
        return (root, query, cb) -> cb.like(root.get("title"), "%" + title + "%");
    }

    @Override
    public Specification<Player> filterByRace(Race race) {
        if (race == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("race"), race);
    }

    @Override
    public Specification<Player> filterByProfession(Profession profession) {
        if (profession == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("profession"), profession);
    }

    @Override
    public Specification<Player> filterByExperience(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min == null && max == null) {
                return null;
            }
            if (min == null) {
                return cb.lessThanOrEqualTo(root.get("experience"), max);
            }
            if (max == null) {
                return cb.greaterThanOrEqualTo(root.get("experience"), min);
            }
            return cb.between(root.get("experience"), min, max);
        };
    }

    @Override
    public Specification<Player> filterByLevel(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min == null && max == null) {
                return null;
            }
            if (min == null) {
                return cb.lessThanOrEqualTo(root.get("level"), max);
            }
            if (max == null) {
                return cb.greaterThanOrEqualTo(root.get("level"), min);
            }
            return cb.between(root.get("level"), min, max);
        };
    }

    @Override
    public Specification<Player> filterByBirthday(Long after, Long before) {
        return (root, query, cb) -> {
            if (after == null && before == null) {
                return null;
            }
            if (after == null) {
                Date beforeDate = new Date(before);
                return cb.lessThanOrEqualTo(root.get("birthday"), beforeDate);
            }
            if (before == null) {
                Date afterDate = new Date(after);
                return cb.greaterThanOrEqualTo(root.get("birthday"), afterDate);
            }
            Date beforeDate = new Date(before);
            Date afterDate = new Date(after);
            return cb.between(root.get("birthday"), afterDate, beforeDate);
        };
    }

    @Override
    public Specification<Player> filterByBan(Boolean isBanned) {
        if (isBanned == null) {
            return null;
        }
        return (root, query, cb) -> {
            if (isBanned) {
                return cb.isTrue(root.get("banned"));
            } else {
                return cb.isFalse(root.get("banned"));
            }
        };
    }

    private Integer getPlayerLevel(Player player) {
        Integer exp = player.getExperience();
        return (int) ((Math.sqrt(2500 + 200 * exp) - 50) / 100);
    }

    private Integer getExperienceUntilNextLevel(Player player) {
        Integer lvl = player.getLevel();
        Integer exp = player.getExperience();

        return 50 * (lvl + 1) * (lvl + 2) - exp;
    }
}
