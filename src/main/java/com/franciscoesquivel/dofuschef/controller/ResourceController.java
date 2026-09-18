package com.franciscoesquivel.dofuschef.controller;

import com.franciscoesquivel.dofuschef.dto.ResourceFilter;
import com.franciscoesquivel.dofuschef.dto.ResourceRequest;
import com.franciscoesquivel.dofuschef.dto.ResourceResponse;
import com.franciscoesquivel.dofuschef.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService svc;

    @PostMapping
    public ResponseEntity<ResourceResponse> insert(@Valid @RequestBody ResourceRequest request) {
        ResourceResponse response = svc.insert(request);
        URI location = URI.create("/api/v1/resources/" + response.ankamaId());
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/load")
    public ResponseEntity<Boolean> load() {
        return ResponseEntity.ok(svc.load());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ResourceResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(svc.findById(id));
    }

    @GetMapping("/ankamaid/{id}")
    public ResponseEntity<ResourceResponse> findByAnkId(@PathVariable int id) {
        return ResponseEntity.ok(svc.findByAnkamaId(id));
    }

    @GetMapping()
    public ResponseEntity<Page<ResourceResponse>> findAll(@ModelAttribute ResourceFilter filter, Pageable p) {
        return ResponseEntity.ok(svc.findAll(filter, p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}
