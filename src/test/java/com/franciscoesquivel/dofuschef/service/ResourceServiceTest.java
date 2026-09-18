package com.franciscoesquivel.dofuschef.service;

import com.franciscoesquivel.dofuschef.dto.ResourceFilter;
import com.franciscoesquivel.dofuschef.dto.ResourceResponse;
import com.franciscoesquivel.dofuschef.mapper.ResourceEntityMapper;
import com.franciscoesquivel.dofuschef.model.Resource;
import com.franciscoesquivel.dofuschef.repository.IResourceRepository;
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
class ResourceServiceTest {

    @Mock
    IResourceRepository dao;

    @Mock
    ResourceEntityMapper mapper;

    @Mock
    DofusdudeService ddService;

    @InjectMocks
    ResourceService service;

    @Test
    void findAll_whenNoFiltersProvided_thenReturnsAllResults() {
        ResourceFilter filter = new ResourceFilter(null);
        Pageable pageable = PageRequest.of(0, 20);
        Resource resource = new Resource();
        ResourceResponse response = new ResourceResponse(1, "Wheat", "desc", 1, 1, null, null);
        Page<Resource> repoPage = new PageImpl<>(List.of(resource), pageable, 1);
        when(dao.findByFilters(null, pageable)).thenReturn(repoPage);
        when(mapper.toResponse(resource)).thenReturn(response);

        Page<ResourceResponse> result = service.findAll(filter, pageable);

        assertThat(result.getContent()).containsExactly(response);
    }

    @Test
    void findAll_whenNameFilterProvided_thenPassesNameToRepository() {
        ResourceFilter filter = new ResourceFilter("wheat");
        Pageable pageable = PageRequest.of(0, 20);
        when(dao.findByFilters(any(), any())).thenReturn(Page.empty(pageable));

        service.findAll(filter, pageable);

        verify(dao).findByFilters("wheat", pageable);
    }

    @Test
    void findAll_whenResultIsEmptyPage_thenReturnsEmptyPage() {
        ResourceFilter filter = new ResourceFilter("nonexistent");
        Pageable pageable = PageRequest.of(0, 20);
        when(dao.findByFilters(any(), any())).thenReturn(Page.empty(pageable));

        Page<ResourceResponse> result = service.findAll(filter, pageable);

        assertThat(result.getContent()).isEmpty();
    }
}
