package com.franciscoesquivel.dofuschef.Resources;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IResourceRepository extends CrudRepository<Resource, Integer> {
    Optional<Resource> findByAnkamaId(int id);
}
