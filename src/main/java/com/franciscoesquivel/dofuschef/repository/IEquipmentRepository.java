package com.franciscoesquivel.dofuschef.repository;

import com.franciscoesquivel.dofuschef.model.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEquipmentRepository extends JpaRepository<Equipment, Long> {
    Optional<Equipment> findByAnkamaId(int id);

    Page<Equipment> findByNameLike(String name, Pageable pageable);
}
