package com.franciscoesquivel.dofuschef.equipment;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IEquipmentRepository extends CrudRepository<Equipment, Long> {
    Optional<Equipment> findByAnkamaID(int id);
}
