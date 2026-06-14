package com.franciscoesquivel.dofuschef.service;

import com.dofusdude.client.ApiException;
import com.franciscoesquivel.dofuschef.model.Equipment;
import com.franciscoesquivel.dofuschef.repository.IEquipmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EquipmentService {

    @Autowired IEquipmentRepository equipmentRepository;
    @Autowired DofusdudeService ddService;

    public boolean load() {
        try {
            List<Equipment> equipments = ddService.findAllEquipments();
            if(equipments.isEmpty())
                throw new IllegalStateException("Load failed: no equipments found");
            equipmentRepository.saveAll(equipments);
            return true;
        } catch (ApiException e) {
            log.error("Load failed: could not retrieve resources : {}", e.getMessage());
            return false;
        }
    }
}
