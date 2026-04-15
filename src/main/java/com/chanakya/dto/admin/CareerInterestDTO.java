package com.chanakya.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerInterestDTO {

    private Long careerId;

    private String careerName;

    private Long interestCount;

    private Double interestPercentage;

    private Integer popularityScore;
}
