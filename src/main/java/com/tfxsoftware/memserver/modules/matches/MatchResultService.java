package com.tfxsoftware.memserver.modules.matches;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchResultService {

    private final MatchResultRepository matchResultRepository;

    @Transactional
    public MatchResult save(MatchResult matchResult) {
        return matchResultRepository.save(matchResult);
    }

    @Transactional(readOnly = true)
    public Optional<MatchResult> findByMatchId(UUID matchId) {
        return matchResultRepository.findById(matchId);
    }
}
