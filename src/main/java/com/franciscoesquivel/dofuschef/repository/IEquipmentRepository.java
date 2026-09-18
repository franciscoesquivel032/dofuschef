package com.franciscoesquivel.dofuschef.repository;

import com.franciscoesquivel.dofuschef.model.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IEquipmentRepository extends JpaRepository<Equipment, Long> {
    Optional<Equipment> findByAnkamaId(int id);

    @Query("""
            SELECT e FROM Equipment e
            LEFT JOIN e.type t
            WHERE (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:type IS NULL OR t.name = :type)
              AND (:isWeapon IS NULL OR e.isWeapon = :isWeapon)
              AND (:minLevel IS NULL OR e.level >= :minLevel)
              AND (:maxLevel IS NULL OR e.level <= :maxLevel)
            """)
    Page<Equipment> findByFilters(
            @Param("name") String name,
            @Param("type") String type,
            @Param("isWeapon") Boolean isWeapon,
            @Param("minLevel") Integer minLevel,
            @Param("maxLevel") Integer maxLevel,
            Pageable pageable);
}
