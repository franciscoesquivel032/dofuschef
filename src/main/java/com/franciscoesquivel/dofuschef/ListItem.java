package com.franciscoesquivel.dofuschef;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class ListItem {
    private int ankamaId;
    private String name;
    private TranslatedID type;
    private int level;
    private ImageUrls imageUrls;
    private Recipe recipe;
    private String description;
    private ConditionNode conditions;
    private List<Effect> effects;
    private boolean isWeapon;
    private int pods;
    private TranslatedID parentSet;
    private int critChance;
    private int critBonus;
    private int maxCastPerTurn;
    private int apCost;
    private Range range;
}
