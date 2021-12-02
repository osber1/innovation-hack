package com.learning.security.mapper;

import org.mapstruct.Mapper;

import com.learning.security.model.Image;
import com.learning.security.model.ImageDTO;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageDTO imageToDTO(Image user);
}
