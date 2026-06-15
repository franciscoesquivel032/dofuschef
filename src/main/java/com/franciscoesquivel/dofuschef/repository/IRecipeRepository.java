package com.franciscoesquivel.dofuschef.repository;

import com.franciscoesquivel.dofuschef.model.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface IRecipeRepository extends CrudRepository<Recipe, Integer> {
}
