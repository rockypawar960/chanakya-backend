package com.chanakya.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDTO {
    private Long id;
    private String title;
    private String description;
    private String type;
    private String url;
    private String thumbnailUrl;
    private Long careerId;
    private String careerName;  // 🔥 YEH FIELD ADD KARO - iski wajah se error aa raha hai
    private Integer duration;
    private Boolean isActive;
}