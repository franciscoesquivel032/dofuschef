package com.franciscoesquivel.dofuschef.dto;

import jakarta.validation.constraints.PositiveOrZero;

public record CraftLineUpdateRequest(
        @PositiveOrZero
        Integer currentQuantity,

        @PositiveOrZero
        Integer kamasValue
) {}
