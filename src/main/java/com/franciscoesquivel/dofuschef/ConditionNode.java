package com.franciscoesquivel.dofuschef;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ConditionNode {
    private ConditionRelation conditionRelation;
    private ConditionLeaf conditionLeaf;
}
