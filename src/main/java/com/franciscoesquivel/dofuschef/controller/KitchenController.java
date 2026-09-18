package com.franciscoesquivel.dofuschef.controller;

import com.franciscoesquivel.dofuschef.dto.KitchenRequest;
import com.franciscoesquivel.dofuschef.dto.KitchenResponse;
import com.franciscoesquivel.dofuschef.service.KitchenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/kitchens")
@RequiredArgsConstructor
public class KitchenController {

    private final KitchenService kitchenService;

    @PostMapping
    public ResponseEntity<KitchenResponse> create(@Valid @RequestBody KitchenRequest request) {
        KitchenResponse response = kitchenService.create(request);
        URI location = URI.create("/kitchens/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KitchenResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(kitchenService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<KitchenResponse>> findAll() {
        return ResponseEntity.ok(kitchenService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<KitchenResponse> update(@PathVariable Long id, @Valid @RequestBody KitchenRequest request) {
        return ResponseEntity.ok(kitchenService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        kitchenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
