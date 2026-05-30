package com.franciscoesquivel.dofuschef.Resources;

import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ResourceService {

    @Autowired IResourceRepository dao;

    public boolean insert(Resource r) throws IllegalArgumentException {
        boolean insert = false;
        if (r == null) throw new IllegalArgumentException("Resource cannot be null");
        if(this.dao.findById(r.getId()).isEmpty()){
            this.dao.save(r);
            insert = true;
        } else throw new IllegalArgumentException("Resource already exists");

        return insert;
    }

    public boolean delete(int id) throws Exception {
        if(id < 0) throw new IllegalArgumentException("ID cannot be lower than 0");
        boolean exists = this.dao.findById(id).isPresent();
        if(exists) {
            this.dao.deleteById(id);
            return true;
        } else throw new NotFoundException("Resource with id" + id + " not found");
    }

    public Optional<Resource> findById(int id) {
        return this.dao.findById(id);
    }

    public Optional<Resource> findByAnkamaId(int id) {
        return this.dao.findByAnkamaId(id);
    }
}
