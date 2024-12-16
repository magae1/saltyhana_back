package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class IdResponse<T> {
    private T id;
}
