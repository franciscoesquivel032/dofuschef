package com.franciscoesquivel.dofuschef.service;

import com.dofusdude.client.ApiException;
import com.franciscoesquivel.dofuschef.dto.EquipmentFilter;
import com.franciscoesquivel.dofuschef.dto.EquipmentResponse;
import com.franciscoesquivel.dofuschef.mapper.EquipmentEntityMapper;
import com.franciscoesquivel.dofuschef.model.Equipment;
import com.franciscoesquivel.dofuschef.repository.IEquipmentRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class EquipmentService {

    @Autowired IEquipmentRepository dao;
    @Autowired DofusdudeService ddService;
    @Autowired EquipmentEntityMapper mapper;

    public boolean load() {
        try {
            List<Equipment> equipments = ddService.findAllEquipments();
            if(equipments.isEmpty())
                throw new IllegalStateException("Load failed: no equipments found");
            dao.saveAll(equipments);
            return true;
        } catch (ApiException e) {
            log.error("Load failed: could not retrieve resources : {}", e.getMessage());
            return false;
        }
    }

    public EquipmentResponse findByAnkamaId(int ankamaId) {
        if(ankamaId < 0) throw new IllegalArgumentException("ID cannot be negative");
        return dao.findByAnkamaId(ankamaId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Equipment with ankamaId " + ankamaId + " not found"));
    }

    public EquipmentResponse findById(Long id) {
        if(id < 0) throw new IllegalArgumentException("ID cannot be negative");
        return dao.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Equipment with id " + id + " not found"));
    }

    public Page<EquipmentResponse> findAll(EquipmentFilter filter, Pageable pageable) {
        Page<Equipment> result = dao.findByFilters(
                filter.name(),
                filter.type(),
                filter.isWeapon(),
                filter.minLevel(),
                filter.maxLevel(),
                pageable);
        return result.map(mapper::toResponse);
    }

}
