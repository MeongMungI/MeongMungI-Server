package com.meongmungi.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalkRequest {
    private LocalDate walkDate;
    private Integer calories;
    private Double distanceKm;
    private LocalTime startTime;
    private LocalTime endTime;
}
