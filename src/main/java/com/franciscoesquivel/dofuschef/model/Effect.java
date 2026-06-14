package com.franciscoesquivel.dofuschef.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Effect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int intMinimum;
    private int intMaximum;
    @OneToOne
    private EffectType type;
    private boolean ignoreIntMin;
    private boolean ignoreIntMax;
    private String formatted;
}
