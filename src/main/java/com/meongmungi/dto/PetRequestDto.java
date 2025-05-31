package com.meongmungi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meongmungi.entity.Pet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetRequestDto {

    @NotBlank(message = "이름을 반드시 입력해야 합니다.")
    private String name;

    @NotBlank(message = "견종을 반드시 입력해야 합니다.")
    private String breed;

    @NotNull(message = "성별을 반드시 선택해야 합니다.")
    private Pet.Gender gender;

    @NotNull(message = "몸무게를 입력해야 합니다.")
    @Positive(message = "몸무게는 0보다 커야 합니다.")
    private Double weight;

    @NotNull(message = "생일을 입력해야 합니다.")
    @Past(message = "생일은 과거 날짜여야 합니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
