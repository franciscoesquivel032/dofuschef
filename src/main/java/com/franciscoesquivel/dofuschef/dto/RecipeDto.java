package com.franciscoesquivel.dofuschef.dto;

import java.util.Set;

public record RecipeDto(
        Set<RecipeLineDto> lines
) {}
