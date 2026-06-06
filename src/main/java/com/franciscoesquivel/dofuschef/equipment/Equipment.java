package com.franciscoesquivel.dofuschef.equipment;

import com.franciscoesquivel.dofuschef.ImageUrls;
import com.franciscoesquivel.dofuschef.Recipe;
import com.franciscoesquivel.dofuschef.TranslatedID;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Equipment {
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    @EqualsAndHashCode.Include
    private int ankamaId;
    private String name;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private TranslatedID type;
    @OneToOne
    private ImageUrls images;
    private int level;
    @Column(length = 10000)
    private String description;
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
    private boolean isWeapon;
}
