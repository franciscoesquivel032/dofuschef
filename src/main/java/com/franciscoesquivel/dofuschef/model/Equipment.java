package com.franciscoesquivel.dofuschef.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Equipment extends AnkamaItem {

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "type_id")
    private TranslatedID type;

    private boolean isWeapon;
}
