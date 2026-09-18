package com.franciscoesquivel.dofuschef.controller;

import com.franciscoesquivel.dofuschef.dto.CraftLineResponse;
import com.franciscoesquivel.dofuschef.dto.CraftLineUpdateRequest;
import com.franciscoesquivel.dofuschef.dto.CraftRequest;
import com.franciscoesquivel.dofuschef.dto.CraftResponse;
import com.franciscoesquivel.dofuschef.exception.ItemNotFoundException;
import com.franciscoesquivel.dofuschef.exception.RecipeNotFoundException;
import com.franciscoesquivel.dofuschef.exception.ResourceNotFoundException;
import com.franciscoesquivel.dofuschef.security.AuthTokenFilter;
import com.franciscoesquivel.dofuschef.service.CraftService;
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
        controllers = CraftController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthTokenFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
class CraftControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CraftService craftService;

    @Test
    void create_whenValid_thenReturnsCreatedWithLocation() throws Exception {
        CraftResponse response = new CraftResponse(5L, 200, List.of());
        when(craftService.create(eq(1L), any())).thenReturn(response);

        mockMvc.perform(post("/kitchens/1/crafts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new CraftRequest(200))))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/kitchens/1/crafts/5"));
    }

    @Test
    void create_whenItemNotFound_thenReturnsNotFound() throws Exception {
        when(craftService.create(eq(1L), any())).thenThrow(new ItemNotFoundException("Item not found with ankamaId: 999"));

        mockMvc.perform(post("/kitchens/1/crafts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new CraftRequest(999))))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_whenItemHasNoRecipe_thenReturnsUnprocessableEntity() throws Exception {
        when(craftService.create(eq(1L), any())).thenThrow(new RecipeNotFoundException("Item with ankamaId 200 has no recipe"));

        mockMvc.perform(post("/kitchens/1/crafts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new CraftRequest(200))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void findById_whenExists_thenReturnsOk() throws Exception {
        when(craftService.findById(1L, 5L)).thenReturn(new CraftResponse(5L, 200, List.of()));

        mockMvc.perform(get("/kitchens/1/crafts/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L));
    }

    @Test
    void findById_whenNotOwned_thenReturnsNotFound() throws Exception {
        when(craftService.findById(1L, 5L)).thenThrow(new ResourceNotFoundException("Craft not found with id: 5"));

        mockMvc.perform(get("/kitchens/1/crafts/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAll_thenReturnsOkWithList() throws Exception {
        when(craftService.findAll(1L)).thenReturn(List.of(new CraftResponse(5L, 200, List.of())));

        mockMvc.perform(get("/kitchens/1/crafts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5L));
    }

    @Test
    void delete_whenExists_thenReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/kitchens/1/crafts/5"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateLine_whenValid_thenReturnsOkWithUpdatedLine() throws Exception {
        CraftLineResponse response = new CraftLineResponse(9L, 300, 10, 10, true, 0);
        when(craftService.updateLine(eq(1L), eq(5L), eq(9L), any())).thenReturn(response);

        mockMvc.perform(patch("/kitchens/1/crafts/5/lines/9")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new CraftLineUpdateRequest(10, null))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }
}
