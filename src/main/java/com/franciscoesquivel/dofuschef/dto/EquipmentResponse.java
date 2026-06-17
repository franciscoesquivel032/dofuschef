package com.franciscoesquivel.dofuschef.dto;

public record EquipmentResponse(
        int ankamaId,
        String name,
        String type,
        String description,
        int level,
        boolean isWeapon,
        ImageUrlsDto images,
        RecipeDto recipe
) {}
