package com.franciscoesquivel.dofuschef.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @EqualsAndHashCode.Include
    private int ankamaId;
    private String name;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "type_id")
    private TranslatedID type;
    @OneToOne(cascade = CascadeType.ALL)
    private ImageUrls images;
    private int level;
    @Column(length = 10000)
    private String description;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
    private boolean isWeapon;
}
