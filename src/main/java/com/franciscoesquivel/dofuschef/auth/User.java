package com.franciscoesquivel.dofuschef.auth;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @EqualsAndHashCode.Include
    @NonNull
    @Length(min = 6, max = 20)
    @Column(unique = true)
    private String username;
    @Length(min = 6, max = 20)
    private String password;
    @EqualsAndHashCode.Include
    @Email
    @Column(unique = true)
    private String email;
}
