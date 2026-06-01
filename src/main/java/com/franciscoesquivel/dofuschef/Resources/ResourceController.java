package com.franciscoesquivel.dofuschef.Resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/resources")
public class ResourceController {
    @Autowired ResourceService svc;

    @PostMapping
    public ResponseEntity<Boolean> insert(@RequestBody Resource r) {
        try {
            this.svc.insert(r);
            return ResponseEntity.status(HttpStatus.CREATED).body(true);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/load")
    public ResponseEntity<Boolean> load() {
        try {
            this.svc.load();
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Resource> findById(@PathVariable int id) {
        return ResponseEntity.of(this.svc.findById(id));
    }

    @GetMapping("/ankid/{id}")
    public ResponseEntity<Resource> findByAnkId(@PathVariable int id) {
        return ResponseEntity.of(this.svc.findByAnkamaId(id));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> delete(int id) {
        try {
            this.svc.delete(id);
            return ResponseEntity.ok().body(true);
        } catch (Exception e) {
          return ResponseEntity.badRequest().body(false);
        }
    }

}
