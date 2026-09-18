package com.franciscoesquivel.dofuschef.mapper;

import com.franciscoesquivel.dofuschef.dto.KitchenRequest;
import com.franciscoesquivel.dofuschef.dto.KitchenResponse;
import com.franciscoesquivel.dofuschef.model.Kitchen;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface KitchenMapper {

    KitchenResponse toResponse(Kitchen kitchen);

    void updateEntity(KitchenRequest request, @MappingTarget Kitchen kitchen);
}
