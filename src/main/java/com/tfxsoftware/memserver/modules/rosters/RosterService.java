package com.tfxsoftware.memserver.modules.rosters;

import com.tfxsoftware.memserver.modules.players.Player;
import com.tfxsoftware.memserver.modules.players.PlayerRepository;
import com.tfxsoftware.memserver.modules.players.PlayerService;
import com.tfxsoftware.memserver.modules.rosters.dto.CreateRosterDto;
import com.tfxsoftware.memserver.modules.rosters.dto.RosterResponse;
import com.tfxsoftware.memserver.modules.users.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RosterService {

    private final RosterRepository rosterRepository;
    private final PlayerRepository playerRepository;
    private final PlayerService playerService;

    @Transactional
    public RosterResponse createRoster(User owner, CreateRosterDto dto) {
        boolean hasRoster = rosterRepository.existsByOwnerId(owner.getId());

        if (!hasRoster && !dto.getRegion().equals(owner.getRegion())) {
            throw new IllegalArgumentException("The first roster must be in the same region as the user.");
        }

        if (dto.getPlayerIds().size() != 5) {
            throw new IllegalArgumentException("A roster must have exactly 5 players.");
        }

        List<Player> players = playerRepository.findAllById(dto.getPlayerIds());

        if (players.size() != dto.getPlayerIds().size()) {
            throw new IllegalArgumentException("One or more players not found.");
        }

        for (Player player : players) {
            if (player.getOwner() == null || !player.getOwner().getId().equals(owner.getId())) {
                throw new IllegalArgumentException("Player " + player.getNickname() + " does not belong to you.");
            }
            if (player.getRoster() != null) {
                throw new IllegalArgumentException("Player " + player.getNickname() + " is already assigned to a roster.");
            }
        }

        Roster roster = Roster.builder()
                .name(dto.getName())
                .region(dto.getRegion())
                .owner(owner)
                .players(players)
                .build();

        Roster savedRoster = rosterRepository.save(roster);

        for (Player player : players) {
            player.setRoster(savedRoster);
        }
        playerRepository.saveAll(players);

        log.info("Roster {} created for user {}", savedRoster.getName(), owner.getUsername());
        return mapToResponse(savedRoster);
    }

    public RosterResponse mapToResponse(Roster roster) {
        return RosterResponse.builder()
                .id(roster.getId())
                .name(roster.getName())
                .region(roster.getRegion())
                .activity(roster.getActivity())
                .cohesion(roster.getCohesion())
                .morale(roster.getMorale())
                .energy(roster.getEnergy())
                .players(roster.getPlayers() != null ? 
                        roster.getPlayers().stream().map(playerService::mapToResponse).toList() : 
                        List.of())
                .build();
    }
}
