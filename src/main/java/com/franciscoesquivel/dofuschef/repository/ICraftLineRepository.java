package com.franciscoesquivel.dofuschef.repository;

import com.franciscoesquivel.dofuschef.model.CraftLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ICraftLineRepository extends JpaRepository<CraftLine, Long> {

    @Query("SELECT cl FROM CraftLine cl WHERE cl.id = :lineId AND cl.craft.id = :craftId " +
            "AND cl.craft.kitchen.id = :kitchenId AND cl.craft.kitchen.user.username = :username")
    Optional<CraftLine> findOwned(
            @Param("lineId") Long lineId,
            @Param("craftId") Long craftId,
            @Param("kitchenId") Long kitchenId,
            @Param("username") String username);
}
