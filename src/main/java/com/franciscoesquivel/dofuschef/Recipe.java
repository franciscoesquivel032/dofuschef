package com.franciscoesquivel.dofuschef;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Recipe {
    @NotNull
    @EqualsAndHashCode.Include
    private int itemAnkamaId;
    private String itemSubtype;
    private int quantity;
}
