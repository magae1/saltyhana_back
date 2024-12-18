package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ExistsResponseDTO {
    private boolean exists;
}
