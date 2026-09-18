package com.franciscoesquivel.dofuschef.dto;

import jakarta.validation.constraints.Positive;

public record CraftRequest(
        @Positive(message = "itemAnkamaId must be positive")
        int itemAnkamaId
) {}
