package com.franciscoesquivel.dofuschef.controller;

import com.franciscoesquivel.dofuschef.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/equipments")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @PostMapping("/load")
    public ResponseEntity<Boolean> load() {
        return ResponseEntity.ok(equipmentService.load());
    }
}
