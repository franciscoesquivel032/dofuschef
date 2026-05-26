package com.franciscoesquivel.dofuschef;

import lombok.*;

import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class ConditionRelation {
    private boolean isOperand;
    private String relation;
    private HashSet<ConditionNode> children;
}
