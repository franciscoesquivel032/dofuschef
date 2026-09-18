package com.franciscoesquivel.dofuschef.controller;

import com.franciscoesquivel.dofuschef.dto.ResourceFilter;
import com.franciscoesquivel.dofuschef.dto.ResourceResponse;
import com.franciscoesquivel.dofuschef.security.AuthTokenFilter;
import com.franciscoesquivel.dofuschef.service.ResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ResourceController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthTokenFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
class ResourceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ResourceService service;

    @Test
    void findAll_whenCalledWithoutParams_thenReturnsOkWithPagedContent() throws Exception {
        ResourceResponse response = new ResourceResponse(1, "Wheat", "desc", 1, 1, null, null);
        Page<ResourceResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);
        when(service.findAll(any(), any())).thenReturn(page);

        mockMvc.perform(get("/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Wheat"));
    }

    @Test
    void findAll_whenNameParamProvided_thenServiceReceivesFilterWithName() throws Exception {
        when(service.findAll(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/resources").param("name", "wheat"))
                .andExpect(status().isOk());

        org.mockito.ArgumentCaptor<ResourceFilter> captor = org.mockito.ArgumentCaptor.forClass(ResourceFilter.class);
        verify(service).findAll(captor.capture(), any());
        assertThat(captor.getValue().name()).isEqualTo("wheat");
    }
}
