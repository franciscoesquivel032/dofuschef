package com.franciscoesquivel.dofuschef.mapper;

import com.franciscoesquivel.dofuschef.dto.RecipeDto;
import com.franciscoesquivel.dofuschef.dto.RecipeLineDto;
import com.franciscoesquivel.dofuschef.model.Recipe;
import com.franciscoesquivel.dofuschef.model.RecipeLine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecipeEntityMapper {

    RecipeDto toDto(Recipe recipe);

    RecipeLineDto toDto(RecipeLine recipeLine);
}
