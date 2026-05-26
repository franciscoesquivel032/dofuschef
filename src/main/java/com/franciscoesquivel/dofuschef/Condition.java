package com.franciscoesquivel.dofuschef;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Condition {
    private String operator;
    private int intValue;
    private TranslatedID element;
}
