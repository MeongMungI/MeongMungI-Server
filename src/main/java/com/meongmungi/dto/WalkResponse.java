package com.meongmungi.dto;

import com.meongmungi.entity.Walk;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalkResponse {
    private Long id;
    private LocalDate walkDate;
    private Integer calories;
    private Double distanceKm;
    private LocalTime startTime;
    private LocalTime endTime;

    public static WalkResponse fromEntity(Walk w) {
        return WalkResponse.builder()
                .id(w.getId())
                .walkDate(w.getWalkDate())
                .calories(w.getCalories())
                .distanceKm(w.getDistanceKm())
                .startTime(w.getStartTime())
                .endTime(w.getEndTime())
                .build();
    }
}
