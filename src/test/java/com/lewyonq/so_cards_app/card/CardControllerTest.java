package com.lewyonq.so_cards_app.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lewyonq.so_cards_app.card.dto.CardDetailDto;
import com.lewyonq.so_cards_app.card.dto.CardRequestDto;
import com.lewyonq.so_cards_app.card.dto.CardResponseDto;
import com.lewyonq.so_cards_app.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
class CardControllerTest {
    CardRequestDto requestDto;
    CardResponseDto responseDto;
    CardDetailDto cardDetailDto;

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = new CardRequestDto();
        requestDto.setQuestion("test question");
        requestDto.setAnswer("test answer");

        responseDto = new CardResponseDto();
        responseDto.setId(1L);
        responseDto.setQuestion("test question");
        responseDto.setAnswer("test answer");

        cardDetailDto = new CardDetailDto();
        cardDetailDto.setId(1L);
        cardDetailDto.setQuestion("test question");
        cardDetailDto.setAnswer("test answer");
    }

    @Test
    void createCard_success() throws Exception {

        when(cardService.createCard(eq(1L), any(CardRequestDto.class))).thenReturn(cardDetailDto);
        mockMvc.perform(post("/api/v1/card/add-to-deck/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getCardDetails_Success() throws Exception {
        Long id = 1L;
        when(cardService.getCardDetails(id)).thenReturn(cardDetailDto);

        mockMvc.perform(get("/api/v1/card/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question").value("test question"))
                .andExpect(jsonPath("$.answer").value("test answer"));
    }

    @Test
    void getCardDetails_Failure_ResourceNotFound() throws Exception {
        Long id = 1L;
        when(cardService.getCardDetails(id)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/api/v1/card/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCard_Success() throws Exception {
        Long id = 1L;
        when(cardService.updateCard(id, requestDto)).thenReturn(cardDetailDto);

        mockMvc.perform(put("/api/v1/card/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDetailDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCard_Success() throws Exception {
        Long id = 1L;
        doNothing().when(cardService).deleteCard(id);

        mockMvc.perform(delete("/api/v1/card/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCard_Failure() throws Exception {
        Long id = 2L;
        doThrow(ResourceNotFoundException.class).when(cardService).deleteCard(id);

        mockMvc.perform(delete("/api/v1/card/" + id))
                .andExpect(status().isNotFound());
    }
}