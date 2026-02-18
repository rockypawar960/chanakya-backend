package com.chanakya.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {

    private Long id;
    private String questionText;
    private String questionType;
    private Integer sequenceNumber;
    private List<QuestionOptionDTO> options;
}
