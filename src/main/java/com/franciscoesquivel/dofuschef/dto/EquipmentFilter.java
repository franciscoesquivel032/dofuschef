package com.franciscoesquivel.dofuschef.dto;

public record EquipmentFilter(
        String name,
        String type,
        Boolean isWeapon,
        Integer minLevel,
        Integer maxLevel
) {}
