package com.franciscoesquivel.dofuschef.mapper;

import com.franciscoesquivel.dofuschef.dto.ImageUrlsDto;
import com.franciscoesquivel.dofuschef.model.ImageUrls;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageUrlsEntityMapper {

    ImageUrlsDto toDto(ImageUrls imageUrls);
}
