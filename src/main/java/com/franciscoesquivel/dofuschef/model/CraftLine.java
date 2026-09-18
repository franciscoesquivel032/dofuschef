package com.franciscoesquivel.dofuschef.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "craft_lines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CraftLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private int itemAnkamaId;

    @Column(nullable = false)
    private int targetQuantity;

    @Column(nullable = false)
    @Builder.Default
    private int currentQuantity = 0;

    @Column(nullable = false)
    @Builder.Default
    private boolean completed = false;

    @Column(nullable = false)
    @Builder.Default
    private int kamasValue = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "craft_id", nullable = false)
    private Craft craft;
}
