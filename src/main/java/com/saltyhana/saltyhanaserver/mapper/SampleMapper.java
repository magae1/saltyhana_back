package com.saltyhana.saltyhanaserver.mapper;


import com.saltyhana.saltyhanaserver.dto.SampleDTO;
import com.saltyhana.saltyhanaserver.entity.Sample;


public class SampleMapper {
    public static Sample toEntity(SampleDTO sampleDTO) {
        return Sample.builder()
                .id(sampleDTO.getId())
                .name(sampleDTO.getName())
                .build();
    }

    public static SampleDTO toDTO(Sample sample) {
        return SampleDTO.builder()
                .id(sample.getId())
                .name(sample.getName())
                .build();
    }
}
