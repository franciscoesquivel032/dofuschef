package com.franciscoesquivel.dofuschef;

import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Resource {
    @EqualsAndHashCode.Include
    private int ankamaId;
    private String name;
    private String description;
    private TranslatedID translatedID;
    private int level;
    private int pods;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private HashSet<ImageUrls> images;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "id")
    private ArrayList<Effect> effects;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "id")
    private ArrayList<ConditionNode> conditions;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "id")
    private ArrayList<Recipe> recipe;
}
