package com.cgi.store.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cgi.store.dto.request.FinsDtoRequest;
import com.cgi.store.dto.response.FinsDtoResponse;
import com.cgi.store.exceptions.GlobalExceptionHandler;
import com.cgi.store.exceptions.ResourceNotFoundException;
import com.cgi.store.services.FinsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(FinsController.class)
@Import(GlobalExceptionHandler.class)
class FinsControllerTest {

    private static final String BASE_PATH = "/products/fins";
    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 999L;
    private static final String BRAND_NAME_TEST = "Test";
    private static final String BRAND_NAME_NEW = "NewFin";
    private static final String BRAND_NAME_UPDATED = "Updated";
    private static final String BRAND_NAME_VALID = "Valid";
    private static final String BRAND_NAME_INVALID_SHORT = "ab";
    private static final BigDecimal PRICE_TEN = BigDecimal.TEN;
    private static final BigDecimal PRICE_ONE = BigDecimal.ONE;
    private static final BigDecimal PRICE_TWO = BigDecimal.valueOf(2);
    private static final BigDecimal PRICE_NEGATIVE = BigDecimal.valueOf(-1);
    private static final int STATUS_BAD_REQUEST = 400;
    private static final int STATUS_NOT_FOUND = 404;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FinsService finsService;

    @Nested
    @DisplayName("GET all fins")
    class GetAllFins {

        @Test
        @DisplayName("should return 200 and list when getting all fins")
        void getAllFins_returns200AndList() throws Exception {
            FinsDtoResponse dto = FinsDtoResponse.builder().id(EXISTING_ID).brandName(BRAND_NAME_TEST).price(PRICE_TEN).build();
            when(finsService.getAllFins()).thenReturn(List.of(dto));

            mockMvc.perform(get(BASE_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].id").value(EXISTING_ID.intValue()))
                    .andExpect(jsonPath("$[0].brandName").value(BRAND_NAME_TEST));
        }
    }

    @Nested
    @DisplayName("GET fin by ID")
    class GetFinsById {

        @Test
        @DisplayName("should return 200 and DTO when fin exists")
        void getFinsById_whenExists_returns200AndDto() throws Exception {
            FinsDtoResponse dto = FinsDtoResponse.builder().id(EXISTING_ID).brandName(BRAND_NAME_TEST).price(PRICE_TEN).build();
            when(finsService.getFinsById(EXISTING_ID)).thenReturn(dto);

            mockMvc.perform(get(BASE_PATH + "/" + EXISTING_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(EXISTING_ID.intValue()))
                    .andExpect(jsonPath("$.brandName").value(BRAND_NAME_TEST));
        }

        @Test
        @DisplayName("should return 404 and error body when fin does not exist")
        void getFinsById_whenNotExists_returns404AndErrorResponse() throws Exception {
            when(finsService.getFinsById(NOT_EXISTING_ID)).thenThrow(new ResourceNotFoundException("Fin", NOT_EXISTING_ID));

            mockMvc.perform(get(BASE_PATH + "/" + NOT_EXISTING_ID))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(STATUS_NOT_FOUND))
                    .andExpect(jsonPath("$.error").value("Not Found"))
                    .andExpect(jsonPath("$.message", containsString(NOT_EXISTING_ID.toString())));
        }
    }

    @Nested
    @DisplayName("POST create fin")
    class CreateFins {

        @Test
        @DisplayName("should return 200 and created DTO when body is valid")
        void createFins_withValidBody_returns200() throws Exception {
            FinsDtoRequest request = FinsDtoRequest.builder().brandName(BRAND_NAME_NEW).price(PRICE_ONE).build();
            FinsDtoResponse response = FinsDtoResponse.builder().id(EXISTING_ID).brandName(BRAND_NAME_NEW).price(PRICE_ONE).build();
            when(finsService.createFins(any(FinsDtoRequest.class))).thenReturn(response);

            mockMvc.perform(post(BASE_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(EXISTING_ID.intValue()))
                    .andExpect(jsonPath("$.brandName").value(BRAND_NAME_NEW));
        }

        @Test
        @DisplayName("should return 400 when body is invalid")
        void createFins_withInvalidBody_returns400() throws Exception {
            FinsDtoRequest invalidRequest = FinsDtoRequest.builder().brandName("").price(null).build();

            mockMvc.perform(post(BASE_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(STATUS_BAD_REQUEST))
                    .andExpect(jsonPath("$.error").value("Bad Request"));
        }
    }

    @Nested
    @DisplayName("PUT update fin")
    class UpdateFins {

        @Test
        @DisplayName("should return 200 and updated DTO when fin exists")
        void updateFins_whenExists_returns200() throws Exception {
            FinsDtoRequest request = FinsDtoRequest.builder().brandName(BRAND_NAME_UPDATED).price(PRICE_TWO).build();
            FinsDtoResponse response = FinsDtoResponse.builder().id(EXISTING_ID).brandName(BRAND_NAME_UPDATED).price(PRICE_TWO).build();
            when(finsService.updateFins(eq(EXISTING_ID), any(FinsDtoRequest.class))).thenReturn(response);

            mockMvc.perform(put(BASE_PATH + "/" + EXISTING_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.brandName").value(BRAND_NAME_UPDATED));
        }

        @Test
        @DisplayName("should return 404 when fin does not exist")
        void updateFins_whenNotExists_returns404() throws Exception {
            FinsDtoRequest request = FinsDtoRequest.builder().brandName(BRAND_NAME_VALID).price(PRICE_ONE).build();
            when(finsService.updateFins(eq(NOT_EXISTING_ID), any(FinsDtoRequest.class)))
                    .thenThrow(new ResourceNotFoundException("Fin", NOT_EXISTING_ID));

            mockMvc.perform(put(BASE_PATH + "/" + NOT_EXISTING_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(STATUS_NOT_FOUND));
        }

        @Test
        @DisplayName("should return 400 when body is invalid")
        void updateFins_withInvalidBody_returns400() throws Exception {
            FinsDtoRequest invalidRequest = FinsDtoRequest.builder()
                    .brandName(BRAND_NAME_INVALID_SHORT)
                    .price(PRICE_NEGATIVE)
                    .build();

            mockMvc.perform(put(BASE_PATH + "/" + EXISTING_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(STATUS_BAD_REQUEST));
        }
    }

    @Nested
    @DisplayName("DELETE fin")
    class DeleteFins {

        @Test
        @DisplayName("should return 200 when fin is deleted")
        void deleteFins_returns200() throws Exception {
            mockMvc.perform(delete(BASE_PATH + "/" + EXISTING_ID))
                    .andExpect(status().isOk());
            verify(finsService).deleteFins(EXISTING_ID);
        }
    }
}
