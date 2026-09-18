package com.franciscoesquivel.dofuschef.mapper;

import com.franciscoesquivel.dofuschef.dto.CraftLineResponse;
import com.franciscoesquivel.dofuschef.model.CraftLine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CraftLineMapper {

    CraftLineResponse toResponse(CraftLine craftLine);
}
