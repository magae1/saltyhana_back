package com.saltyhana.saltyhanaserver.service;


import java.util.List;
import java.util.stream.Collectors;

import com.saltyhana.saltyhanaserver.mapper.IconMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.saltyhana.saltyhanaserver.dto.IconResponseDTO;
import com.saltyhana.saltyhanaserver.entity.Icon;
import com.saltyhana.saltyhanaserver.repository.IconRepository;


@Service
@RequiredArgsConstructor
public class IconService {

  private final IconRepository iconRepo;

  public List<IconResponseDTO> findAllGoalIcons() {
    List<Icon> iconList = iconRepo.findByNameStartsWith("goal");
    return iconList.stream()
        .map(IconMapper::toIconResponseDTO)
        .collect(Collectors.toList());
  }

}
