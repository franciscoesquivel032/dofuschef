package com.franciscoesquivel.dofuschef.mapper;

import com.dofusdude.client.model.ListItem;
import com.franciscoesquivel.dofuschef.model.Recipe;
import com.franciscoesquivel.dofuschef.model.RecipeLine;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public interface ListItemMapper<T> {
    T map(ListItem li);

    default Recipe buildRecipe(ListItem li) {
        var liRecipe = li.getRecipe() != null && !li.getRecipe().isEmpty()
                ? li.getRecipe()
                : Collections.<com.dofusdude.client.model.Recipe>emptyList();
        Set<RecipeLine> lines = liRecipe.isEmpty()
                ? Collections.emptySet()
                : liRecipe.stream()
                .filter(r -> r.getItemAnkamaId() != null && r.getQuantity() != null)
                .map(r -> new RecipeLine(r.getQuantity(), r.getItemAnkamaId()))
                .collect(Collectors.toUnmodifiableSet());
        return Recipe.builder().lines(lines).build();
    }
}
