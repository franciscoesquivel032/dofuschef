package com.franciscoesquivel.dofuschef.mapper;

import com.franciscoesquivel.dofuschef.dto.ResourceRequest;
import com.franciscoesquivel.dofuschef.dto.ResourceResponse;
import com.franciscoesquivel.dofuschef.model.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ImageUrlsEntityMapper.class, RecipeEntityMapper.class})
public interface ResourceEntityMapper {

    ResourceResponse toResponse(Resource resource);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "recipe", ignore = true)
    Resource toEntity(ResourceRequest request);
}
