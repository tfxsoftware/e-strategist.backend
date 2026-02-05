package com.tfxsoftware.memserver.modules.matches.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMatchDto {
    @NotNull
    private UUID homeRosterId;
    @NotNull
    private UUID awayRosterId;
    @NotNull
    private LocalDateTime scheduledTime;
    private UUID eventId;
}
