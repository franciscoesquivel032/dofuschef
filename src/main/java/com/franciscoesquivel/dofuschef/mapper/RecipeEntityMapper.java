package com.franciscoesquivel.dofuschef.mapper;

import com.franciscoesquivel.dofuschef.dto.AnkamaItemDto;
import com.franciscoesquivel.dofuschef.dto.RecipeDto;
import com.franciscoesquivel.dofuschef.dto.RecipeLineDto;
import com.franciscoesquivel.dofuschef.model.AnkamaItem;
import com.franciscoesquivel.dofuschef.model.Recipe;
import com.franciscoesquivel.dofuschef.model.RecipeLine;
import com.franciscoesquivel.dofuschef.repository.IEquipmentRepository;
import com.franciscoesquivel.dofuschef.repository.IResourceRepository;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Slf4j
@Mapper(componentModel = "spring", uses = {ImageUrlsEntityMapper.class})
public abstract class RecipeEntityMapper {

    @Autowired
    private IEquipmentRepository equipmentRepository;

    @Autowired
    private IResourceRepository resourceRepository;

    public abstract RecipeDto toDto(Recipe recipe);

    @Mapping(target = "item", source = "itemAnkamaId", qualifiedByName = "resolveItem")
    public abstract RecipeLineDto toDto(RecipeLine recipeLine);

    @Named("resolveItem")
    public AnkamaItemDto resolveItem(int itemAnkamaId) {
        log.debug("Resolving item ankamaId={}", itemAnkamaId);
        Optional<? extends AnkamaItem> found = resourceRepository.findByAnkamaId(itemAnkamaId)
                .map(e -> (AnkamaItem) e);
        if (found.isEmpty()) {
            found = equipmentRepository.findByAnkamaId(itemAnkamaId)
                    .map(r -> (AnkamaItem) r);
        }
        log.debug("Item found={}", found.isPresent());
        return found.map(item -> new AnkamaItemDto(
                item.getAnkamaId(),
                item.getName(),
                item.getDescription(),
                item.getLevel(),
                item.getImages() != null ? new com.franciscoesquivel.dofuschef.dto.ImageUrlsDto(
                        item.getImages().getIcon(),
                        item.getImages().getSd(),
                        item.getImages().getHq(),
                        item.getImages().getHd()
                ) : null
        )).orElse(null);
    }
}
