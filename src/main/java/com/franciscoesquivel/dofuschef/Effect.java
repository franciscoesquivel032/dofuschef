package com.franciscoesquivel.dofuschef;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Effect {
    private int intMinimum;
    private int intMaximum;
    private EffectType type;
    private boolean ignoreIntMin;
    private boolean ignoreIntMax;
    private String formatted;
}
