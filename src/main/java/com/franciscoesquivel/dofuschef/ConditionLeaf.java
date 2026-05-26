package com.franciscoesquivel.dofuschef;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ConditionLeaf {
    private boolean isOperand;
    private Condition condition;
}
