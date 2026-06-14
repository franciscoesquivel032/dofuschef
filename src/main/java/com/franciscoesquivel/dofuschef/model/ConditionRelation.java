package com.franciscoesquivel.dofuschef.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ConditionRelation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private boolean isOperand;
    private String relation;
    private HashSet<ConditionNode> children;
}
