package com.franciscoesquivel.dofuschef.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ResourceRequest(
        @NotNull @Positive int ankamaId,
        String name,
        String description,
        int level,
        int pods
) {}
