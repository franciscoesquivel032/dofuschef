package com.franciscoesquivel.dofuschef.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @EqualsAndHashCode.Include
    @NonNull
    @Length(min = 6, max = 20)
    private String username;
    @Length(min = 6, max = 20)
    private String password;
    @EqualsAndHashCode.Include
    @Email
    private String email;
}
