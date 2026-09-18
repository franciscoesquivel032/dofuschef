package com.franciscoesquivel.dofuschef.service.impl;

import com.franciscoesquivel.dofuschef.dto.KitchenRequest;
import com.franciscoesquivel.dofuschef.dto.KitchenResponse;
import com.franciscoesquivel.dofuschef.exception.ResourceNotFoundException;
import com.franciscoesquivel.dofuschef.mapper.KitchenMapper;
import com.franciscoesquivel.dofuschef.model.Kitchen;
import com.franciscoesquivel.dofuschef.model.User;
import com.franciscoesquivel.dofuschef.repository.IKitchenRepository;
import com.franciscoesquivel.dofuschef.security.CurrentUserProvider;
import com.franciscoesquivel.dofuschef.service.KitchenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KitchenServiceImpl implements KitchenService {

    private final IKitchenRepository kitchenRepository;
    private final KitchenMapper mapper;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public KitchenResponse create(KitchenRequest request) {
        User user = currentUserProvider.getCurrentUser();
        Kitchen kitchen = Kitchen.builder()
                .title(request.title())
                .description(request.description())
                .user(user)
                .build();
        return mapper.toResponse(kitchenRepository.save(kitchen));
    }

    @Override
    public KitchenResponse findById(Long id) {
        return mapper.toResponse(findOwned(id));
    }

    @Override
    public List<KitchenResponse> findAll() {
        return kitchenRepository.findByUser_Username(currentUserProvider.getCurrentUsername())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public KitchenResponse update(Long id, KitchenRequest request) {
        Kitchen kitchen = findOwned(id);
        mapper.updateEntity(request, kitchen);
        return mapper.toResponse(kitchenRepository.save(kitchen));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        kitchenRepository.delete(findOwned(id));
    }

    private Kitchen findOwned(Long id) {
        return kitchenRepository.findByIdAndUser_Username(id, currentUserProvider.getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Kitchen not found with id: " + id));
    }
}
