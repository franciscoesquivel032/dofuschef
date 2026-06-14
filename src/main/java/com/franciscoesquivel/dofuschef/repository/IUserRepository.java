package com.franciscoesquivel.dofuschef.repository;

import com.franciscoesquivel.dofuschef.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    boolean existsUserByEmail(String email);
    boolean existsUserByUsername(String email);
}
