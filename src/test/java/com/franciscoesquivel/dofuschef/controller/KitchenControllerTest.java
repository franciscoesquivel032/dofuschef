package com.franciscoesquivel.dofuschef.controller;

import com.franciscoesquivel.dofuschef.dto.KitchenRequest;
import com.franciscoesquivel.dofuschef.dto.KitchenResponse;
import com.franciscoesquivel.dofuschef.exception.ResourceNotFoundException;
import com.franciscoesquivel.dofuschef.security.AuthTokenFilter;
import com.franciscoesquivel.dofuschef.service.KitchenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = KitchenController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthTokenFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
class KitchenControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    KitchenService kitchenService;

    @Test
    void create_whenValidRequest_thenReturnsCreatedWithLocation() throws Exception {
        KitchenResponse response = new KitchenResponse(1L, "My Kitchen", "desc");
        when(kitchenService.create(any())).thenReturn(response);

        mockMvc.perform(post("/kitchens")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new KitchenRequest("My Kitchen", "desc"))))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/kitchens/1"))
                .andExpect(jsonPath("$.title").value("My Kitchen"));
    }

    @Test
    void create_whenTitleBlank_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/kitchens")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new KitchenRequest("", "desc"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_whenExists_thenReturnsOk() throws Exception {
        when(kitchenService.findById(1L)).thenReturn(new KitchenResponse(1L, "K", "d"));

        mockMvc.perform(get("/kitchens/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void findById_whenNotFound_thenReturnsNotFound() throws Exception {
        when(kitchenService.findById(99L)).thenThrow(new ResourceNotFoundException("Kitchen not found with id: 99"));

        mockMvc.perform(get("/kitchens/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Kitchen not found with id: 99"));
    }

    @Test
    void findAll_thenReturnsOkWithList() throws Exception {
        when(kitchenService.findAll()).thenReturn(List.of(new KitchenResponse(1L, "K", "d")));

        mockMvc.perform(get("/kitchens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void update_whenValid_thenReturnsOk() throws Exception {
        when(kitchenService.update(eq(1L), any())).thenReturn(new KitchenResponse(1L, "New", "d2"));

        mockMvc.perform(put("/kitchens/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new KitchenRequest("New", "d2"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New"));
    }

    @Test
    void delete_whenExists_thenReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/kitchens/1"))
                .andExpect(status().isNoContent());
    }
}
