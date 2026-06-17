package com.franciscoesquivel.dofuschef.mapper;

import com.franciscoesquivel.dofuschef.dto.EquipmentResponse;
import com.franciscoesquivel.dofuschef.model.Equipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ImageUrlsEntityMapper.class, RecipeEntityMapper.class})
public interface EquipmentEntityMapper {

    @Mapping(target = "type", source = "type.name")
    @Mapping(target = "isWeapon", source = "weapon")
    EquipmentResponse toResponse(Equipment equipment);
}
