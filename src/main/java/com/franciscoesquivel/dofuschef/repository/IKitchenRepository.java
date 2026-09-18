package com.franciscoesquivel.dofuschef.repository;

import com.franciscoesquivel.dofuschef.model.Kitchen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IKitchenRepository extends JpaRepository<Kitchen, Long> {
    List<Kitchen> findByUser_Username(String username);
    Optional<Kitchen> findByIdAndUser_Username(Long id, String username);
}
