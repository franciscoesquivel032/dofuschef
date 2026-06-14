package com.franciscoesquivel.dofuschef.repository;

import com.franciscoesquivel.dofuschef.model.Equipment;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IEquipmentRepository extends CrudRepository<Equipment, Long> {
    Optional<Equipment> findByAnkamaId(int id);
}
