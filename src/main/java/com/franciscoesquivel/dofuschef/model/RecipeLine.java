package com.franciscoesquivel.dofuschef.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RecipeLine {
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private int itemAnkamaId;
}
