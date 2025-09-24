package edu.trincoll.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.trincoll.model.Item;
import edu.trincoll.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private Item item1;
    private Item item2;

    @BeforeEach
    void setup() {
        item1 = new Item("Laptop", "Electronics");
        item1.setId(1L);
        item2 = new Item("Chair", "Furniture");
        item2.setId(2L);
    }

    @Test
    void testGetAllItems() throws Exception {
        given(itemService.findAll()).willReturn(Arrays.asList(item1, item2));

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Laptop"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Chair"));
    }

    @Test
    void testGetItemById() throws Exception {
        given(itemService.findById(1L)).willReturn(Optional.of(item1));

        mockMvc.perform(get("/api/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Laptop"));
    }

    @Test
    void testCreateItem() throws Exception {
        Item newItem = new Item("Phone", "Electronics");
        newItem.setId(3L);

        given(itemService.save(any(Item.class))).willReturn(newItem);

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("Phone"));
    }

    @Test
    void testUpdateItem() throws Exception {
        Item updated = new Item("Laptop Pro", "Electronics");
        updated.setId(1L);

        given(itemService.update(eq(1L), any(Item.class))).willReturn(Optional.of(updated));

        mockMvc.perform(put("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Laptop Pro"));
    }

    @Test
    void testDeleteItem() throws Exception {
        doNothing().when(itemService).deleteById(1L);

        mockMvc.perform(delete("/api/items/1"))
                .andExpect(status().isNoContent());
    }
}
