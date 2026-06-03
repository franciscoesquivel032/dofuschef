package com.franciscoesquivel.dofuschef.resources;
import com.franciscoesquivel.dofuschef.ImageUrls;
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
    private String description;
    private int level;
    private int pods;
    @OneToOne
    private ImageUrls images;
}
