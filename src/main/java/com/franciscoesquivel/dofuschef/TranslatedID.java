package com.franciscoesquivel.dofuschef;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class TranslatedID {
    @EqualsAndHashCode.Include
    private int id;
    private String name;
}
