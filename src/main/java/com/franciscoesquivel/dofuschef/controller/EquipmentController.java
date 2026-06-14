package com.franciscoesquivel.dofuschef.controller;

import com.franciscoesquivel.dofuschef.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/equipments")
public class EquipmentController {

    @Autowired EquipmentService equipmentService;

    @PostMapping("/load")
    public ResponseEntity<Boolean> load() {
        try {
            this.equipmentService.load();
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }
}
