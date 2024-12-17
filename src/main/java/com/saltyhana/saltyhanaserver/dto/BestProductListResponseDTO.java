package com.saltyhana.saltyhanaserver.dto;

import com.saltyhana.saltyhanaserver.enums.ProductEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BestProductListResponseDTO {
    private Long id;
    private String title;
    private ProductEnum type;
    private String subtitle;
    private String imageUrl;
    private String description;
}
