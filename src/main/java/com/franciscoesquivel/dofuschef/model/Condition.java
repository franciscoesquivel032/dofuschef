package com.franciscoesquivel.dofuschef.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Condition {
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String operator;
    private int intValue;
    @OneToOne
    private TranslatedID element;
    @OneToOne
    private ConditionLeaf conditionLeaf;
}
