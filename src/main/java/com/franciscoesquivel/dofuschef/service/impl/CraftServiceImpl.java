package com.franciscoesquivel.dofuschef.service.impl;

import com.franciscoesquivel.dofuschef.dto.CraftLineResponse;
import com.franciscoesquivel.dofuschef.dto.CraftLineUpdateRequest;
import com.franciscoesquivel.dofuschef.dto.CraftRequest;
import com.franciscoesquivel.dofuschef.dto.CraftResponse;
import com.franciscoesquivel.dofuschef.exception.ItemNotFoundException;
import com.franciscoesquivel.dofuschef.exception.RecipeNotFoundException;
import com.franciscoesquivel.dofuschef.exception.ResourceNotFoundException;
import com.franciscoesquivel.dofuschef.mapper.CraftLineMapper;
import com.franciscoesquivel.dofuschef.mapper.CraftMapper;
import com.franciscoesquivel.dofuschef.model.AnkamaItem;
import com.franciscoesquivel.dofuschef.model.Craft;
import com.franciscoesquivel.dofuschef.model.CraftLine;
import com.franciscoesquivel.dofuschef.model.Kitchen;
import com.franciscoesquivel.dofuschef.model.Recipe;
import com.franciscoesquivel.dofuschef.repository.ICraftLineRepository;
import com.franciscoesquivel.dofuschef.repository.ICraftRepository;
import com.franciscoesquivel.dofuschef.repository.IEquipmentRepository;
import com.franciscoesquivel.dofuschef.repository.IKitchenRepository;
import com.franciscoesquivel.dofuschef.repository.IResourceRepository;
import com.franciscoesquivel.dofuschef.security.CurrentUserProvider;
import com.franciscoesquivel.dofuschef.service.CraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CraftServiceImpl implements CraftService {

    private final ICraftRepository craftRepository;
    private final IKitchenRepository kitchenRepository;
    private final ICraftLineRepository craftLineRepository;
    private final IResourceRepository resourceRepository;
    private final IEquipmentRepository equipmentRepository;
    private final CraftMapper craftMapper;
    private final CraftLineMapper craftLineMapper;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public CraftResponse create(Long kitchenId, CraftRequest request) {
        Kitchen kitchen = findOwnedKitchen(kitchenId);
        AnkamaItem item = resolveItem(request.itemAnkamaId());
        Recipe recipe = item.getRecipe();
        if (recipe == null) {
            throw new RecipeNotFoundException("Item with ankamaId " + request.itemAnkamaId() + " has no recipe");
        }

        Craft craft = Craft.builder()
                .itemAnkamaId(request.itemAnkamaId())
                .kitchen(kitchen)
                .build();
        recipe.getLines().forEach(recipeLine -> craft.getLines().add(
                CraftLine.builder()
                        .itemAnkamaId(recipeLine.getItemAnkamaId())
                        .targetQuantity(recipeLine.getQuantity())
                        .craft(craft)
                        .build()
        ));

        return craftMapper.toResponse(craftRepository.save(craft));
    }

    @Override
    public CraftResponse findById(Long kitchenId, Long craftId) {
        return craftMapper.toResponse(findOwnedCraft(kitchenId, craftId));
    }

    @Override
    public List<CraftResponse> findAll(Long kitchenId) {
        return craftRepository.findByKitchen_IdAndKitchen_User_Username(kitchenId, currentUserProvider.getCurrentUsername())
                .stream()
                .map(craftMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long kitchenId, Long craftId) {
        craftRepository.delete(findOwnedCraft(kitchenId, craftId));
    }

    @Override
    @Transactional
    public CraftLineResponse updateLine(Long kitchenId, Long craftId, Long lineId, CraftLineUpdateRequest request) {
        CraftLine line = craftLineRepository.findOwned(lineId, craftId, kitchenId, currentUserProvider.getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Craft line not found with id: " + lineId));

        if (request.currentQuantity() != null) {
            line.setCurrentQuantity(request.currentQuantity());
        }
        if (request.kamasValue() != null) {
            line.setKamasValue(request.kamasValue());
        }
        line.setCompleted(line.getCurrentQuantity() >= line.getTargetQuantity());

        return craftLineMapper.toResponse(craftLineRepository.save(line));
    }

    private Kitchen findOwnedKitchen(Long kitchenId) {
        return kitchenRepository.findByIdAndUser_Username(kitchenId, currentUserProvider.getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Kitchen not found with id: " + kitchenId));
    }

    private Craft findOwnedCraft(Long kitchenId, Long craftId) {
        return craftRepository.findByIdAndKitchen_IdAndKitchen_User_Username(craftId, kitchenId, currentUserProvider.getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Craft not found with id: " + craftId));
    }

    private AnkamaItem resolveItem(int itemAnkamaId) {
        Optional<? extends AnkamaItem> found = resourceRepository.findByAnkamaId(itemAnkamaId)
                .map(r -> (AnkamaItem) r);
        if (found.isEmpty()) {
            found = equipmentRepository.findByAnkamaId(itemAnkamaId)
                    .map(e -> (AnkamaItem) e);
        }
        return found.orElseThrow(() -> new ItemNotFoundException("Item not found with ankamaId: " + itemAnkamaId));
    }
}
