package com.lewyonq.so_cards_app.deck;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lewyonq.so_cards_app.deck.dto.DeckDetailDto;
import com.lewyonq.so_cards_app.deck.dto.DeckRequestDto;
import com.lewyonq.so_cards_app.deck.dto.DeckResponseDto;
import com.lewyonq.so_cards_app.exception.ResourceNotFoundException;
import com.lewyonq.so_cards_app.model.entity.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeckController.class)
class DeckControllerTest {
    DeckRequestDto requestDto;
    Deck deck;
    Deck savedDeck;
    DeckDetailDto deckDetailDto;
    DeckResponseDto responseDto;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DeckService deckService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = new DeckRequestDto();
        requestDto.setName("test name");
        requestDto.setDescription("test description");

//        deck = Deck.builder()
//                .id(1L)
//                .name("test name")
//                .description("test description")
//                .build();
//
//        savedDeck = Deck.builder()
//                .id(1L)
//                .name("test name")
//                .description("test description")
//                .build();
//
        responseDto = new DeckResponseDto();
        responseDto.setId(1L);
        responseDto.setName("test name");
        responseDto.setDescription("test description");

        deckDetailDto = new DeckDetailDto();
        deckDetailDto.setId(1L);
        deckDetailDto.setName("test name");
        deckDetailDto.setDescription("test description");
    }

//    @Test
//    void createDeck_success() throws Exception {
//        when(deckService.createDeck(requestDto)).thenReturn(deckDetailDto);
//        System.out.println("abcd" + deckDetailDto);
//
//        mockMvc.perform(post("/api/v1/deck")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1L));
//    }

    @Test
    void getAllDecks_Success() throws Exception {
        when(deckService.getAllDecks()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/deck"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("test name"))
                .andExpect(jsonPath("$[0].description").value("test description"));
    }

    @Test
    void getDeckDetails_Success() throws Exception {
        Long id = 1L;
        when(deckService.getDeckDetails(id)).thenReturn(deckDetailDto);

        mockMvc.perform(get("/api/v1/deck/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test name"))
                .andExpect(jsonPath("$.description").value("test description"));
    }

    @Test
    void getDeckDetails_Failure_ResourceNotFound() throws Exception {
        Long id = 1L;
        when(deckService.getDeckDetails(id)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/api/v1/deck/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateDeck_Success() throws Exception {
        Long id = 1L;
        when(deckService.updateDeck(id, requestDto)).thenReturn(deckDetailDto);

        mockMvc.perform(put("/api/v1/deck/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deckDetailDto)))
                .andExpect(status().isOk());
    }

//    @Test
//    void updateDeck_Failure_ResourceNotFound() throws Exception {
//        Long id = 2L;
//        when(deckService.updateDeck(id, requestDto)).thenThrow(ResourceNotFoundException.class);
//
//        mockMvc.perform(put("/api/v1/deck/" + id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(deckDetailDto)))
//                .andExpect(status().isNotFound());
//    }

    @Test
    void deleteDeck_Success() throws Exception {
        Long id = 1L;
        doNothing().when(deckService).deleteDeck(id);

        mockMvc.perform(delete("/api/v1/deck/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteDeck_Failure() throws Exception {
        Long id = 2L;
        doThrow(ResourceNotFoundException.class).when(deckService).deleteDeck(id);

        mockMvc.perform(delete("/api/v1/deck/" + id))
                .andExpect(status().isNotFound());
    }
}