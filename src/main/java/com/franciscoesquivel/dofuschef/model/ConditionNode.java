package com.franciscoesquivel.dofuschef.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ConditionNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    private ConditionRelation conditionRelation;
    @OneToOne
    private ConditionLeaf conditionLeaf;
}
