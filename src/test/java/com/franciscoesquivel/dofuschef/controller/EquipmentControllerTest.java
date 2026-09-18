package com.franciscoesquivel.dofuschef.controller;

import com.franciscoesquivel.dofuschef.dto.EquipmentFilter;
import com.franciscoesquivel.dofuschef.dto.EquipmentResponse;
import com.franciscoesquivel.dofuschef.security.AuthTokenFilter;
import com.franciscoesquivel.dofuschef.service.EquipmentService;
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
import org.springframework.data.domain.Pageable;
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
        controllers = EquipmentController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthTokenFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
class EquipmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EquipmentService service;

    @Test
    void findAll_whenCalledWithoutParams_thenReturnsOkWithPagedContent() throws Exception {
        EquipmentResponse response = new EquipmentResponse(1, "Amakna Sword", "Sword", "desc", 10, true, null, null);
        Page<EquipmentResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);
        when(service.findAll(any(), any())).thenReturn(page);

        mockMvc.perform(get("/equipments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Amakna Sword"));
    }

    @Test
    void findAll_whenCalledWithFilterParams_thenServiceReceivesParsedFilter() throws Exception {
        when(service.findAll(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/equipments")
                        .param("name", "amu")
                        .param("type", "Amulet")
                        .param("isWeapon", "true")
                        .param("minLevel", "10")
                        .param("maxLevel", "50"))
                .andExpect(status().isOk());

        org.mockito.ArgumentCaptor<EquipmentFilter> captor = org.mockito.ArgumentCaptor.forClass(EquipmentFilter.class);
        verify(service).findAll(captor.capture(), any());
        EquipmentFilter filter = captor.getValue();
        assertThat(filter.name()).isEqualTo("amu");
        assertThat(filter.type()).isEqualTo("Amulet");
        assertThat(filter.isWeapon()).isTrue();
        assertThat(filter.minLevel()).isEqualTo(10);
        assertThat(filter.maxLevel()).isEqualTo(50);
    }

    @Test
    void findAll_whenPageAndSizeProvided_thenPageableReflectsRequestedValues() throws Exception {
        when(service.findAll(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/equipments").param("page", "2").param("size", "5"))
                .andExpect(status().isOk());

        org.mockito.ArgumentCaptor<Pageable> captor = org.mockito.ArgumentCaptor.forClass(Pageable.class);
        verify(service).findAll(any(), captor.capture());
        assertThat(captor.getValue().getPageNumber()).isEqualTo(2);
        assertThat(captor.getValue().getPageSize()).isEqualTo(5);
    }

    @Test
    void findAll_whenLegacyNameRouteRequested_thenNoLongerRoutesToController() throws Exception {
        // GlobalExceptionHandler maps the resulting NoResourceFoundException to 500, not 404.
        mockMvc.perform(get("/equipments/name/amu"))
                .andExpect(status().isInternalServerError());
    }
}
