package com.franciscoesquivel.dofuschef.repository;

import com.franciscoesquivel.dofuschef.model.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IResourceRepository extends JpaRepository<Resource, Long> {
    Optional<Resource> findByAnkamaId(int id);

    @Query("""
            SELECT r FROM Resource r
            WHERE (:name IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')))
            """)
    Page<Resource> findByFilters(@Param("name") String name, Pageable pageable);
}
