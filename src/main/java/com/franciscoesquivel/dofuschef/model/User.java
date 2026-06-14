package com.franciscoesquivel.dofuschef.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @EqualsAndHashCode.Include
    @Length(min = 5, max = 20)
    @Column(unique = true)
    private String username;
    @Length(min = 6)
    private String password;
    @EqualsAndHashCode.Include
    @Email
    @Column(unique = true)
    private String email;
}
