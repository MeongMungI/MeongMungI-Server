package com.meongmungi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meongmungi.entity.Pet;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponseDto {
    private Long id;
    private String name;
    private String breed;
    private Pet.Gender gender;
    private Double weight;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String photoUrl;
}
