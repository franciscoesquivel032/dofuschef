package com.franciscoesquivel.dofuschef.resources;

import com.dofusdude.client.ApiException;
import com.franciscoesquivel.dofuschef.dofusdude.DofusdudeService;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {

    @Autowired IResourceRepository dao;
    @Autowired DofusdudeService ddService;

    public boolean load() {
        try {
            List<Resource> resources = ddService.findAllResources();
            if(resources.isEmpty())
                throw new IllegalStateException("Load failed: no resources found");
            dao.saveAll(resources);
            return true;
        } catch (ApiException e) {
            System.err.println("Load failed: could not retrieve resources : " + e);
            return false;
        }
    }

    public boolean insert(Resource r) throws IllegalArgumentException {
        if (r == null) throw new IllegalArgumentException("Resource cannot be null");
        if(this.dao.findByAnkamaId(r.getAnkamaId()).isEmpty()){
            this.dao.save(r);
            return true;
        } else {
            throw new IllegalArgumentException("Resource already exists");
        }
    }

    public void delete(int id) {
        if (id < 0) throw new IllegalArgumentException("ID cannot be negative");

        if (!dao.existsById(id)) {
            throw new NotFoundException("Resource with id " + id + " not found");
        }

        dao.deleteById(id);
    }

    public Optional<Resource> findById(int id) {
        return this.dao.findById(id);
    }

    public Optional<Resource> findByAnkamaId(int id) {
        return this.dao.findByAnkamaId(id);
    }
}
