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
import com.franciscoesquivel.dofuschef.model.Craft;
import com.franciscoesquivel.dofuschef.model.CraftLine;
import com.franciscoesquivel.dofuschef.model.Equipment;
import com.franciscoesquivel.dofuschef.model.Kitchen;
import com.franciscoesquivel.dofuschef.model.Recipe;
import com.franciscoesquivel.dofuschef.model.RecipeLine;
import com.franciscoesquivel.dofuschef.model.Resource;
import com.franciscoesquivel.dofuschef.repository.ICraftLineRepository;
import com.franciscoesquivel.dofuschef.repository.ICraftRepository;
import com.franciscoesquivel.dofuschef.repository.IEquipmentRepository;
import com.franciscoesquivel.dofuschef.repository.IKitchenRepository;
import com.franciscoesquivel.dofuschef.repository.IResourceRepository;
import com.franciscoesquivel.dofuschef.security.CurrentUserProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CraftServiceImplTest {

    @Mock ICraftRepository craftRepository;
    @Mock IKitchenRepository kitchenRepository;
    @Mock ICraftLineRepository craftLineRepository;
    @Mock IResourceRepository resourceRepository;
    @Mock IEquipmentRepository equipmentRepository;
    @Mock CraftMapper craftMapper;
    @Mock CraftLineMapper craftLineMapper;
    @Mock CurrentUserProvider currentUserProvider;

    @InjectMocks
    CraftServiceImpl service;

    private Kitchen ownedKitchen() {
        return Kitchen.builder().id(1L).title("K").build();
    }

    @Test
    void create_whenKitchenNotOwned_thenThrowsResourceNotFoundException() {
        when(currentUserProvider.getCurrentUsername()).thenReturn("alice");
        when(kitchenRepository.findByIdAndUser_Username(1L, "alice")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(1L, new CraftRequest(100)))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(craftRepository, never()).save(any());
    }

    @Test
    void create_whenItemAnkamaIdResolvesToNothing_thenThrowsItemNotFoundException() {
        when(currentUserProvider.getCurrentUsername()).thenReturn("alice");
        when(kitchenRepository.findByIdAndUser_Username(1L, "alice")).thenReturn(Optional.of(ownedKitchen()));
        when(resourceRepository.findByAnkamaId(100)).thenReturn(Optional.empty());
        when(equipmentRepository.findByAnkamaId(100)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(1L, new CraftRequest(100)))
                .isInstanceOf(ItemNotFoundException.class);
    }

    @Test
    void create_whenResolvedItemHasNoRecipe_thenThrowsRecipeNotFoundException() {
        when(currentUserProvider.getCurrentUsername()).thenReturn("alice");
        when(kitchenRepository.findByIdAndUser_Username(1L, "alice")).thenReturn(Optional.of(ownedKitchen()));
        Resource resource = Resource.builder().ankamaId(100).recipe(null).build();
        when(resourceRepository.findByAnkamaId(100)).thenReturn(Optional.of(resource));

        assertThatThrownBy(() -> service.create(1L, new CraftRequest(100)))
                .isInstanceOf(RecipeNotFoundException.class);
    }

    @Test
    void create_whenEquipmentHasRecipe_thenExpandsOneCraftLinePerRecipeLine() {
        when(currentUserProvider.getCurrentUsername()).thenReturn("alice");
        when(kitchenRepository.findByIdAndUser_Username(1L, "alice")).thenReturn(Optional.of(ownedKitchen()));
        when(resourceRepository.findByAnkamaId(200)).thenReturn(Optional.empty());
        Recipe recipe = Recipe.builder()
                .lines(Set.of(new RecipeLine(5, 300), new RecipeLine(2, 301)))
                .build();
        Equipment equipment = Equipment.builder().ankamaId(200).recipe(recipe).build();
        when(equipmentRepository.findByAnkamaId(200)).thenReturn(Optional.of(equipment));
        when(craftRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        CraftResponse response = new CraftResponse(10L, 200, java.util.List.of());
        when(craftMapper.toResponse(any())).thenReturn(response);

        CraftResponse result = service.create(1L, new CraftRequest(200));

        assertThat(result).isEqualTo(response);
        ArgumentCaptor<Craft> captor = ArgumentCaptor.forClass(Craft.class);
        verify(craftRepository).save(captor.capture());
        Craft savedCraft = captor.getValue();
        assertThat(savedCraft.getItemAnkamaId()).isEqualTo(200);
        assertThat(savedCraft.getLines()).hasSize(2);
        assertThat(savedCraft.getLines()).allSatisfy(line -> {
            assertThat(line.getCurrentQuantity()).isZero();
            assertThat(line.isCompleted()).isFalse();
            assertThat(line.getKamasValue()).isZero();
            assertThat(line.getCraft()).isEqualTo(savedCraft);
        });
        assertThat(savedCraft.getLines().stream().map(CraftLine::getTargetQuantity)).containsExactlyInAnyOrder(5, 2);
        assertThat(savedCraft.getLines().stream().map(CraftLine::getItemAnkamaId)).containsExactlyInAnyOrder(300, 301);
    }

    @Test
    void delete_whenNotOwned_thenThrowsAndNeverDeletes() {
        when(currentUserProvider.getCurrentUsername()).thenReturn("alice");
        when(craftRepository.findByIdAndKitchen_IdAndKitchen_User_Username(2L, 1L, "alice"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(1L, 2L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(craftRepository, never()).delete(any());
    }

    @Test
    void updateLine_whenCurrentQuantityBelowTarget_thenCompletedIsFalse() {
        CraftLine line = CraftLine.builder().id(9L).targetQuantity(10).currentQuantity(0).build();
        when(craftLineRepository.findOwned(9L, 2L, 1L, "alice")).thenReturn(Optional.of(line));
        when(currentUserProvider.getCurrentUsername()).thenReturn("alice");
        when(craftLineRepository.save(line)).thenReturn(line);
        when(craftLineMapper.toResponse(line)).thenReturn(
                new CraftLineResponse(9L, 0, 10, 5, false, 0));

        CraftLineResponse result = service.updateLine(1L, 2L, 9L, new CraftLineUpdateRequest(5, null));

        assertThat(line.getCurrentQuantity()).isEqualTo(5);
        assertThat(line.isCompleted()).isFalse();
        assertThat(result.completed()).isFalse();
    }

    @Test
    void updateLine_whenCurrentQuantityMeetsTarget_thenCompletedBecomesTrue() {
        CraftLine line = CraftLine.builder().id(9L).targetQuantity(10).currentQuantity(0).build();
        when(craftLineRepository.findOwned(9L, 2L, 1L, "alice")).thenReturn(Optional.of(line));
        when(currentUserProvider.getCurrentUsername()).thenReturn("alice");
        when(craftLineRepository.save(line)).thenReturn(line);
        when(craftLineMapper.toResponse(line)).thenReturn(
                new CraftLineResponse(9L, 0, 10, 10, true, 0));

        service.updateLine(1L, 2L, 9L, new CraftLineUpdateRequest(10, null));

        assertThat(line.isCompleted()).isTrue();
    }

    @Test
    void updateLine_whenOnlyKamasValueProvided_thenCurrentQuantityUnchanged() {
        CraftLine line = CraftLine.builder().id(9L).targetQuantity(10).currentQuantity(3).kamasValue(0).build();
        when(craftLineRepository.findOwned(9L, 2L, 1L, "alice")).thenReturn(Optional.of(line));
        when(currentUserProvider.getCurrentUsername()).thenReturn("alice");
        when(craftLineRepository.save(line)).thenReturn(line);
        when(craftLineMapper.toResponse(line)).thenReturn(
                new CraftLineResponse(9L, 0, 10, 3, false, 150));

        service.updateLine(1L, 2L, 9L, new CraftLineUpdateRequest(null, 150));

        assertThat(line.getCurrentQuantity()).isEqualTo(3);
        assertThat(line.getKamasValue()).isEqualTo(150);
    }

    @Test
    void updateLine_whenNotOwned_thenThrowsResourceNotFoundException() {
        when(currentUserProvider.getCurrentUsername()).thenReturn("alice");
        when(craftLineRepository.findOwned(9L, 2L, 1L, "alice")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateLine(1L, 2L, 9L, new CraftLineUpdateRequest(5, null)))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
