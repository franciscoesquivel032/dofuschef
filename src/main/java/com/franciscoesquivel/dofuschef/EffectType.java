package com.franciscoesquivel.dofuschef;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class EffectType {
    @Id
    @EqualsAndHashCode.Include
    private int id;
    private String name;
    private boolean isActive;
    private boolean isMeta;
}
