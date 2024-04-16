package com.jd.eventhall.MainAppBackend.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private String id;
    private String name;
    private String description;
    private String details;
    private String userCreatedId;
    private List<GameSummary> games;
    private long startDate;
    private long endDate;
    
}
