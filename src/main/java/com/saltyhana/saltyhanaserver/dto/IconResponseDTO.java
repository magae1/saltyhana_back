package com.saltyhana.saltyhanaserver.dto;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class IconResponseDTO {

  Long id;
  String name;
  String imageUrl;
  String color;
}
