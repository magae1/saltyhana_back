package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class SampleDTO {
    private Long id;
    private String name;
}
