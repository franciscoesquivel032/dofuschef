package com.franciscoesquivel.dofuschef.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    @NotNull
    @EqualsAndHashCode.Include
    private int ankamaId;
    private String name;
    @Column(length = 20000)
    private String description;
    private int level;
    private int pods;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_urls_id")
    private ImageUrls images;
}
