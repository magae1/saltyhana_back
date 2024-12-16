package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TokenPairResponseDTO {
    private String accessToken;
    private String refreshToken;
}
