package com.franciscoesquivel.dofuschef.dto;

public record AnkamaItemDto(
        int ankamaId,
        String name,
        String description,
        int level,
        ImageUrlsDto images
) {}
