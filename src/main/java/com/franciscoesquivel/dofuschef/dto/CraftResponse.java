package com.franciscoesquivel.dofuschef.dto;

import java.util.List;

public record CraftResponse(
        Long id,
        int itemAnkamaId,
        List<CraftLineResponse> lines
) {}
