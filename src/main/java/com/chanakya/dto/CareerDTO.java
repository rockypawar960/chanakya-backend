package com.chanakya.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerDTO {

    private Long id;
    private String name;
    private String description;
    private String requiredSkills;
    private String jobScope;
    private String salaryRange;
    private String educationPath;
    private String topCompanies;
    private Integer popularityScore;
}
