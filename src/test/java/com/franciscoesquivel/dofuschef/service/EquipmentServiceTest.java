package com.franciscoesquivel.dofuschef.service;

import com.franciscoesquivel.dofuschef.dto.EquipmentFilter;
import com.franciscoesquivel.dofuschef.dto.EquipmentResponse;
import com.franciscoesquivel.dofuschef.mapper.EquipmentEntityMapper;
import com.franciscoesquivel.dofuschef.model.Equipment;
import com.franciscoesquivel.dofuschef.repository.IEquipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceTest {

    @Mock
    IEquipmentRepository dao;

    @Mock
    EquipmentEntityMapper mapper;

    @InjectMocks
    EquipmentService service;

    @Test
    void findAll_whenNoFiltersProvided_thenReturnsAllResults() {
        EquipmentFilter filter = new EquipmentFilter(null, null, null, null, null);
        Pageable pageable = PageRequest.of(0, 20);
        Equipment equipment = new Equipment();
        EquipmentResponse response = new EquipmentResponse(1, "Amakna Sword", "Sword", "desc", 10, true, null, null);
        Page<Equipment> repoPage = new PageImpl<>(List.of(equipment), pageable, 1);
        when(dao.findByFilters(null, null, null, null, null, pageable)).thenReturn(repoPage);
        when(mapper.toResponse(equipment)).thenReturn(response);

        Page<EquipmentResponse> result = service.findAll(filter, pageable);

        assertThat(result.getContent()).containsExactly(response);
    }

    @Test
    void findAll_whenNameFilterProvided_thenPassesNameToRepository() {
        EquipmentFilter filter = new EquipmentFilter("amu", null, null, null, null);
        Pageable pageable = PageRequest.of(0, 20);
        when(dao.findByFilters(any(), any(), any(), any(), any(), any())).thenReturn(Page.empty(pageable));

        service.findAll(filter, pageable);

        verify(dao).findByFilters("amu", null, null, null, null, pageable);
    }

    @Test
    void findAll_whenLevelRangeProvided_thenPassesMinAndMaxLevel() {
        EquipmentFilter filter = new EquipmentFilter(null, null, null, 10, 50);
        Pageable pageable = PageRequest.of(0, 20);
        when(dao.findByFilters(any(), any(), any(), any(), any(), any())).thenReturn(Page.empty(pageable));

        service.findAll(filter, pageable);

        verify(dao).findByFilters(null, null, null, 10, 50, pageable);
    }

    @Test
    void findAll_whenResultIsEmptyPage_thenReturnsEmptyPage() {
        EquipmentFilter filter = new EquipmentFilter("nonexistent", null, null, null, null);
        Pageable pageable = PageRequest.of(0, 20);
        when(dao.findByFilters(any(), any(), any(), any(), any(), any())).thenReturn(Page.empty(pageable));

        Page<EquipmentResponse> result = service.findAll(filter, pageable);

        assertThat(result.getContent()).isEmpty();
    }
}
