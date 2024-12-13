package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BestProductListResponseDTO {
    private int id;
    private String title;
    private String subtitle;
    private String imageSrc;
    private String description;
}
