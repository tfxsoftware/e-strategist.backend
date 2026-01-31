package com.tfxsoftware.memserver.modules.rosters;

import com.tfxsoftware.memserver.modules.rosters.dto.CreateRosterDto;
import com.tfxsoftware.memserver.modules.rosters.dto.RosterResponse;
import com.tfxsoftware.memserver.modules.users.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rosters")
@RequiredArgsConstructor
public class RosterController {

    private final RosterService rosterService;

    @PostMapping()
    public ResponseEntity<RosterResponse> createRoster(@AuthenticationPrincipal User user, @RequestBody @Valid CreateRosterDto dto) {
        return ResponseEntity.ok(rosterService.createRoster(user, dto));
    }
}
