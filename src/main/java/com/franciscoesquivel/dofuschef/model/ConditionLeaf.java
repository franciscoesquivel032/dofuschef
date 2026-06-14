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
public class ConditionLeaf {
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private boolean isOperand;
    @OneToOne
    private Condition condition;
}
