package com.franciscoesquivel.dofuschef.service;

import com.franciscoesquivel.dofuschef.dto.KitchenRequest;
import com.franciscoesquivel.dofuschef.dto.KitchenResponse;

import java.util.List;

public interface KitchenService {
    KitchenResponse create(KitchenRequest request);
    KitchenResponse findById(Long id);
    List<KitchenResponse> findAll();
    KitchenResponse update(Long id, KitchenRequest request);
    void delete(Long id);
}
