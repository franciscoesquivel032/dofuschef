package com.franciscoesquivel.dofuschef.dto;

public record ResourceResponse(
        int ankamaId,
        String name,
        String description,
        int level,
        int pods,
        ImageUrlsDto images,
        RecipeDto recipe
) {}
