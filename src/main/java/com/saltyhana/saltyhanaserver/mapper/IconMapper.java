package com.saltyhana.saltyhanaserver.mapper;


import com.saltyhana.saltyhanaserver.dto.IconResponseDTO;
import com.saltyhana.saltyhanaserver.entity.Icon;


public class IconMapper {

  public static IconResponseDTO toIconResponseDTO(Icon icon) {
    return IconResponseDTO.builder()
        .id(icon.getId())
        .name(icon.getName())
        .imageUrl(icon.getIconImage())
        .color(icon.getColor())
        .build();
  }
}
