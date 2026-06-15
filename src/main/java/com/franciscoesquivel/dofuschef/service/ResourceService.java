package com.franciscoesquivel.dofuschef.service;

import com.dofusdude.client.ApiException;
import com.franciscoesquivel.dofuschef.dto.ResourceRequest;
import com.franciscoesquivel.dofuschef.dto.ResourceResponse;
import com.franciscoesquivel.dofuschef.mapper.ResourceEntityMapper;
import com.franciscoesquivel.dofuschef.model.Resource;
import com.franciscoesquivel.dofuschef.repository.IResourceRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ResourceService {

    private final IResourceRepository dao;
    private final DofusdudeService ddService;
    private final ResourceEntityMapper mapper;

    @Transactional
    public boolean load() {
        try {
            List<Resource> resources = ddService.findAllResources();
            if (resources.isEmpty())
                throw new IllegalStateException("Load failed: no resources found");
            dao.saveAll(resources);
            return true;
        } catch (ApiException e) {
            log.error("Load failed: could not retrieve resources: {}", e.getMessage());
            return false;
        }
    }

    @Transactional
    public ResourceResponse insert(ResourceRequest request) throws IllegalArgumentException {
        if (request == null) throw new IllegalArgumentException("Resource cannot be null");
        if (dao.findByAnkamaId(request.ankamaId()).isPresent())
            throw new IllegalArgumentException("Resource already exists");
        Resource saved = dao.save(mapper.toEntity(request));
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(int id) {
        if (id < 0) throw new IllegalArgumentException("ID cannot be negative");
        if (!dao.existsById(id))
            throw new NotFoundException("Resource with id " + id + " not found");
        dao.deleteById(id);
    }

    public ResourceResponse findById(int id) {
        return dao.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Resource with id " + id + " not found"));
    }

    public ResourceResponse findByAnkamaId(int id) {
        return dao.findByAnkamaId(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Resource with ankamaId " + id + " not found"));
    }
}
