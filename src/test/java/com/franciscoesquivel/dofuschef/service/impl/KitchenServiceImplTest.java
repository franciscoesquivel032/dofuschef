package com.franciscoesquivel.dofuschef.service.impl;

import com.franciscoesquivel.dofuschef.dto.KitchenRequest;
import com.franciscoesquivel.dofuschef.dto.KitchenResponse;
import com.franciscoesquivel.dofuschef.exception.ResourceNotFoundException;
import com.franciscoesquivel.dofuschef.mapper.KitchenMapper;
import com.franciscoesquivel.dofuschef.model.Kitchen;
import com.franciscoesquivel.dofuschef.model.User;
import com.franciscoesquivel.dofuschef.repository.IKitchenRepository;
import com.franciscoesquivel.dofuschef.security.CurrentUserProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KitchenServiceImplTest {

    @Mock
    IKitchenRepository kitchenRepository;

    @Mock
    KitchenMapper mapper;

    @Mock
    CurrentUserProvider currentUserProvider;

    @InjectMocks
    KitchenServiceImpl service;

    @Test
    void create_whenValidRequest_thenSavesKitchenOwnedByCurrentUser() {
        KitchenRequest request = new KitchenRequest("My Kitchen", "desc");
        User user = new User(1L, "alice", "hash", "alice@test.com");
        Kitchen saved = Kitchen.builder().id(10L).title("My Kitchen").description("desc").user(user).build();
        KitchenResponse response = new KitchenResponse(10L, "My Kitchen", "desc");
        when(currentUserProvider.getCurrentUser()).thenReturn(user);
        when(kitchenRepository.save(any())).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        KitchenResponse result = service.create(request);

        assertThat(result).isEqualTo(response);
        ArgumentCaptor<Kitchen> captor = ArgumentCaptor.forClass(Kitchen.class);
        verify(kitchenRepository).save(captor.capture());
        assertThat(captor.getValue().getUser()).isEqualTo(user);
        assertThat(captor.getValue().getTitle()).isEqualTo("My Kitchen");
    }

    @Test
    void findById_whenOwnedByCurrentUser_thenReturnsResponse() {
        Kitchen kitchen = Kitchen.builder().id(5L).title("K").build();
        KitchenResponse response = new KitchenResponse(5L, "K", null);
        when(currentUserProvider.getCurrentUsername()).thenReturn("alice");
        when(kitchenRepository.findByIdAndUser_Username(5L, "alice")).thenReturn(Optional.of(kitchen));
        when(mapper.toResponse(kitchen)).thenReturn(response);

        KitchenResponse result = service.findById(5L);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void findById_whenNotOwnedOrMissing_thenThrowsResourceNotFoundException() {
        when(currentUserProvider.getCurrentUsername()).thenReturn("alice");
        when(kitchenRepository.findByIdAndUser_Username(5L, "alice")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(5L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findAll_thenReturnsOnlyCurrentUsersKitchens() {
        Kitchen kitchen = Kitchen.builder().id(5L).title("K").build();
        KitchenResponse response = new KitchenResponse(5L, "K", null);
        when(currentUserProvider.getCurrentUsername()).thenReturn("alice");
        when(kitchenRepository.findByUser_Username("alice")).thenReturn(List.of(kitchen));
        when(mapper.toResponse(kitchen)).thenReturn(response);

        List<KitchenResponse> result = service.findAll();

        assertThat(result).containsExactly(response);
    }

    @Test
    void update_whenOwned_thenAppliesChangesAndReturnsResponse() {
        Kitchen kitchen = Kitchen.builder().id(5L).title("Old").description("Old desc").build();
        KitchenRequest request = new KitchenRequest("New", "New desc");
        KitchenResponse response = new KitchenResponse(5L, "New", "New desc");
        when(currentUserProvider.getCurrentUsername()).thenReturn("alice");
        when(kitchenRepository.findByIdAndUser_Username(5L, "alice")).thenReturn(Optional.of(kitchen));
        when(kitchenRepository.save(kitchen)).thenReturn(kitchen);
        when(mapper.toResponse(kitchen)).thenReturn(response);

        KitchenResponse result = service.update(5L, request);

        assertThat(result).isEqualTo(response);
        verify(mapper).updateEntity(request, kitchen);
    }

    @Test
    void update_whenNotOwnedOrMissing_thenThrowsResourceNotFoundException() {
        when(currentUserProvider.getCurrentUsername()).thenReturn("alice");
        when(kitchenRepository.findByIdAndUser_Username(5L, "alice")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(5L, new KitchenRequest("New", "desc")))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(mapper, never()).updateEntity(any(), any());
    }

    @Test
    void delete_whenOwned_thenDeletesKitchen() {
        Kitchen kitchen = Kitchen.builder().id(5L).title("K").build();
        when(currentUserProvider.getCurrentUsername()).thenReturn("alice");
        when(kitchenRepository.findByIdAndUser_Username(5L, "alice")).thenReturn(Optional.of(kitchen));

        service.delete(5L);

        verify(kitchenRepository).delete(kitchen);
    }

    @Test
    void delete_whenNotOwnedOrMissing_thenThrowsAndNeverDeletes() {
        when(currentUserProvider.getCurrentUsername()).thenReturn("alice");
        when(kitchenRepository.findByIdAndUser_Username(5L, "alice")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(5L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(kitchenRepository, never()).delete(any());
    }
}
