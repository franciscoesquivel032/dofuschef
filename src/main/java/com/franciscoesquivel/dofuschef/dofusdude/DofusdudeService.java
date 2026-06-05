package com.franciscoesquivel.dofuschef.dofusdude;

import com.dofusdude.client.ApiException;
import com.dofusdude.client.api.ResourcesApi;
import com.dofusdude.client.model.ListItem;
import com.dofusdude.client.model.ListItems;
import com.franciscoesquivel.dofuschef.resources.Resource;
import com.franciscoesquivel.dofuschef.resources.ResourceMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;

@Service
public class DofusdudeService {

    private final ResourcesApi api;
    private final String language;
    private final String gameName;

    public DofusdudeService(ResourcesApi api,
            @Value("${dofusdude.language}") String language,
            @Value("${dofusdude.game-name}") String gameName)
    {
        this.api = api;
        this.language = language;
        this.gameName = gameName;
    }

    public List<Resource> findAllResources() throws ApiException {
        try {
            ListItems response = api.getAllItemsResourcesList(
                    language, gameName, "desc", 1, 200, "", new HashSet<>()
            );
            if(response == null || response.getItems() == null) throw new ApiException();
            List<ListItem> items = response.getItems();
            ListItemMapper<Resource> mapper = new ResourceMapper();
            return items.stream().map(mapper::map).toList();
        } catch (ApiException | ClassCastException e) {
            System.err.println("Error retrieving resources from Dofusdude API : " + e);
            throw new ApiException("Could not fetch resources from external API" + e);
        }
    }

}
