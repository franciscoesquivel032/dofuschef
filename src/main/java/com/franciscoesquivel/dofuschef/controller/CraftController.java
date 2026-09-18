package com.franciscoesquivel.dofuschef.controller;

import com.franciscoesquivel.dofuschef.dto.CraftLineResponse;
import com.franciscoesquivel.dofuschef.dto.CraftLineUpdateRequest;
import com.franciscoesquivel.dofuschef.dto.CraftRequest;
import com.franciscoesquivel.dofuschef.dto.CraftResponse;
import com.franciscoesquivel.dofuschef.service.CraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/kitchens/{kitchenId}/crafts")
@RequiredArgsConstructor
public class CraftController {

    private final CraftService craftService;

    @PostMapping
    public ResponseEntity<CraftResponse> create(@PathVariable Long kitchenId, @Valid @RequestBody CraftRequest request) {
        CraftResponse response = craftService.create(kitchenId, request);
        URI location = URI.create("/kitchens/" + kitchenId + "/crafts/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{craftId}")
    public ResponseEntity<CraftResponse> findById(@PathVariable Long kitchenId, @PathVariable Long craftId) {
        return ResponseEntity.ok(craftService.findById(kitchenId, craftId));
    }

    @GetMapping
    public ResponseEntity<List<CraftResponse>> findAll(@PathVariable Long kitchenId) {
        return ResponseEntity.ok(craftService.findAll(kitchenId));
    }

    @DeleteMapping("/{craftId}")
    public ResponseEntity<Void> delete(@PathVariable Long kitchenId, @PathVariable Long craftId) {
        craftService.delete(kitchenId, craftId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{craftId}/lines/{lineId}")
    public ResponseEntity<CraftLineResponse> updateLine(
            @PathVariable Long kitchenId,
            @PathVariable Long craftId,
            @PathVariable Long lineId,
            @Valid @RequestBody CraftLineUpdateRequest request) {
        return ResponseEntity.ok(craftService.updateLine(kitchenId, craftId, lineId, request));
    }
}
