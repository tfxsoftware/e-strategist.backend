package com.tfxsoftware.memserver.modules.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRegistrationResponse {
    private UUID id;
    private UUID rosterId;
    private String rosterName;
    private UUID eventId;
    private String eventName;
    private LocalDateTime registrationDate;
}
