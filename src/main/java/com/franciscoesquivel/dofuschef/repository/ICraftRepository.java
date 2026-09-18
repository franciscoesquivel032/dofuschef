package com.franciscoesquivel.dofuschef.repository;

import com.franciscoesquivel.dofuschef.model.Craft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICraftRepository extends JpaRepository<Craft, Long> {
    List<Craft> findByKitchen_IdAndKitchen_User_Username(Long kitchenId, String username);
    Optional<Craft> findByIdAndKitchen_IdAndKitchen_User_Username(Long id, Long kitchenId, String username);
}
