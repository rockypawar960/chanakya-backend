package com.chanakya.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackStatisticsDTO {

    private Long totalFeedback;

    private Long openCount;

    private Long inProgressCount;

    private Long resolvedCount;

    private Long closedCount;

    private Double resolutionRate;
}
