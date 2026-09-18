package com.franciscoesquivel.dofuschef.mapper;

import com.franciscoesquivel.dofuschef.dto.CraftResponse;
import com.franciscoesquivel.dofuschef.model.Craft;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CraftLineMapper.class)
public interface CraftMapper {

    CraftResponse toResponse(Craft craft);
}
