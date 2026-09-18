package com.franciscoesquivel.dofuschef.service;

import com.franciscoesquivel.dofuschef.dto.CraftLineResponse;
import com.franciscoesquivel.dofuschef.dto.CraftLineUpdateRequest;
import com.franciscoesquivel.dofuschef.dto.CraftRequest;
import com.franciscoesquivel.dofuschef.dto.CraftResponse;

import java.util.List;

public interface CraftService {
    CraftResponse create(Long kitchenId, CraftRequest request);
    CraftResponse findById(Long kitchenId, Long craftId);
    List<CraftResponse> findAll(Long kitchenId);
    void delete(Long kitchenId, Long craftId);
    CraftLineResponse updateLine(Long kitchenId, Long craftId, Long lineId, CraftLineUpdateRequest request);
}
