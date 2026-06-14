package com.franciscoesquivel.dofuschef.service;

import com.dofusdude.client.ApiException;
import com.dofusdude.client.api.EquipmentApi;
import com.dofusdude.client.api.ResourcesApi;
import com.dofusdude.client.model.ListItem;
import com.dofusdude.client.model.ListItems;
import com.franciscoesquivel.dofuschef.config.ApiContainer;
import com.franciscoesquivel.dofuschef.mapper.EquipmentMapper;
import com.franciscoesquivel.dofuschef.mapper.ListItemMapper;
import com.franciscoesquivel.dofuschef.mapper.ResourceMapper;
import com.franciscoesquivel.dofuschef.model.Equipment;
import com.franciscoesquivel.dofuschef.model.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class DofusdudeService {

    private static final String LANGUAGE = "es";
    private static final String GAME_NAME = "dofus3";
    private static final String SORT_ORDER = "desc";
    private static final int MIN_LVL = 1;
    private static final int MAX_LVL = 200;
    private static final String ENCODING = "";
    private static final Set<String> FILTER_TYPE = Collections.emptySet();

    private final ApiContainer apiContainer;

    public DofusdudeService(ApiContainer apiContainer)
    {
        this.apiContainer = apiContainer;
    }

    public List<Resource> findAllResources() throws ApiException {
        try {
            ListItems response = apiContainer.get(ResourcesApi.class).getAllItemsResourcesList(
                    LANGUAGE, GAME_NAME, SORT_ORDER, MIN_LVL, MAX_LVL, ENCODING, FILTER_TYPE
            );
            if(response == null || response.getItems() == null) throw new ApiException();
            List<ListItem> items = response.getItems();
            ListItemMapper<Resource> mapper = new ResourceMapper();
            return items.stream().map(mapper::map).toList();
        } catch (ApiException | ClassCastException e) {
            log.error("Error retrieving resources from Dofusdude API : {}", e.getMessage());
            throw new ApiException("Could not fetch resources from external API" + e);
        }
    }
    public List<Equipment> findAllEquipments() throws ApiException {
        try {
            ListItems response = apiContainer.get(EquipmentApi.class).getAllItemsEquipmentList(
                    LANGUAGE, GAME_NAME, SORT_ORDER, MIN_LVL, MAX_LVL, ENCODING, FILTER_TYPE
            );
            if(response == null || response.getItems() == null) throw new ApiException();
            List<ListItem> items = response.getItems();
            ListItemMapper<Equipment> mapper = new EquipmentMapper();
            return items.stream().map(mapper::map).toList();
        } catch (ApiException | ClassCastException e) {
            log.error("Error retrieving equipments from Dofusdude API : {}", e.getMessage());
            throw new ApiException("Could not fetch equipments from external API" + e);
        }
    }

}
