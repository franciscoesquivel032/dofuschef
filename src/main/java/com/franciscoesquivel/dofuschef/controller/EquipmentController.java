package com.franciscoesquivel.dofuschef.controller;

import com.franciscoesquivel.dofuschef.dto.EquipmentFilter;
import com.franciscoesquivel.dofuschef.dto.EquipmentResponse;
import com.franciscoesquivel.dofuschef.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipments")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService svc;

    @PostMapping("/load")
    public ResponseEntity<Boolean> load() {
        return ResponseEntity.ok(svc.load());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<EquipmentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(svc.findById(id));
    }

    @GetMapping("/ankamaid/{id}")
    public ResponseEntity<EquipmentResponse> findByAnkamaId(@PathVariable int id) {
        return ResponseEntity.ok(svc.findByAnkamaId(id));
    }

    @GetMapping()
    public ResponseEntity<Page<EquipmentResponse>> findAll(@ModelAttribute EquipmentFilter filter, Pageable p) {
        return ResponseEntity.ok(this.svc.findAll(filter, p));
    }
}
