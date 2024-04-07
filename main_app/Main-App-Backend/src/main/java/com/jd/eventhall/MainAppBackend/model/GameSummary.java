package com.jd.eventhall.MainAppBackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameSummary {
    private int id;
    private String name;
    private String coverUrl;
}
