package com.franciscoesquivel.dofuschef.dto;

public record CraftLineResponse(
        Long id,
        int itemAnkamaId,
        int targetQuantity,
        int currentQuantity,
        boolean completed,
        int kamasValue
) {}
