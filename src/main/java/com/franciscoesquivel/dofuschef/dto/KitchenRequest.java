package com.franciscoesquivel.dofuschef.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record KitchenRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 100)
        String title,

        @Size(max = 2000)
        String description
) {}
