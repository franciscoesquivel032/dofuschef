package com.franciscoesquivel.dofuschef;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Effect {
    @Id
    @Generated
    private int id;
    private int intMinimum;
    private int intMaximum;
    @OneToOne
    private EffectType type;
    private boolean ignoreIntMin;
    private boolean ignoreIntMax;
    private String formatted;
}
