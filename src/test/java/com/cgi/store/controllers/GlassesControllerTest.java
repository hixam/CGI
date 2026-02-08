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

import com.cgi.store.dto.request.GlassesDtoRequest;
import com.cgi.store.dto.response.GlassesDtoResponse;
import com.cgi.store.exceptions.GlobalExceptionHandler;
import com.cgi.store.exceptions.ResourceNotFoundException;
import com.cgi.store.services.GlassesService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(GlassesController.class)
@Import(GlobalExceptionHandler.class)
class GlassesControllerTest {

    private static final String BASE_PATH = "/products/glasses";
    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 999L;
    private static final String BRAND_NAME_TEST = "Test";
    private static final String BRAND_NAME_NEW = "NewGlass";
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
    private GlassesService glassesService;

    @Nested
    @DisplayName("GET all glasses")
    class GetAllGlasses {

        @Test
        @DisplayName("should return 200 and list when getting all glasses")
        void getAllGlasses_returns200AndList() throws Exception {
            GlassesDtoResponse dto = GlassesDtoResponse.builder().id(EXISTING_ID).brandName(BRAND_NAME_TEST).price(PRICE_TEN).build();
            when(glassesService.getAllGlasses()).thenReturn(List.of(dto));

            mockMvc.perform(get(BASE_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].id").value(EXISTING_ID.intValue()))
                    .andExpect(jsonPath("$[0].brandName").value(BRAND_NAME_TEST));
        }
    }

    @Nested
    @DisplayName("GET glass by ID")
    class GetGlassesById {

        @Test
        @DisplayName("should return 200 and DTO when glass exists")
        void getGlassesById_whenExists_returns200AndDto() throws Exception {
            GlassesDtoResponse dto = GlassesDtoResponse.builder().id(EXISTING_ID).brandName(BRAND_NAME_TEST).price(PRICE_TEN).build();
            when(glassesService.getGlassesById(EXISTING_ID)).thenReturn(dto);

            mockMvc.perform(get(BASE_PATH + "/" + EXISTING_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(EXISTING_ID.intValue()))
                    .andExpect(jsonPath("$.brandName").value(BRAND_NAME_TEST));
        }

        @Test
        @DisplayName("should return 404 and error body when glass does not exist")
        void getGlassesById_whenNotExists_returns404AndErrorResponse() throws Exception {
            when(glassesService.getGlassesById(NOT_EXISTING_ID)).thenThrow(new ResourceNotFoundException("Glass", NOT_EXISTING_ID));

            mockMvc.perform(get(BASE_PATH + "/" + NOT_EXISTING_ID))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(STATUS_NOT_FOUND))
                    .andExpect(jsonPath("$.error").value("Not Found"))
                    .andExpect(jsonPath("$.message", containsString(NOT_EXISTING_ID.toString())));
        }
    }

    @Nested
    @DisplayName("POST create glass")
    class CreateGlasses {

        @Test
        @DisplayName("should return 200 and created DTO when body is valid")
        void createGlasses_withValidBody_returns200() throws Exception {
            GlassesDtoRequest request = GlassesDtoRequest.builder().brandName(BRAND_NAME_NEW).price(PRICE_ONE).build();
            GlassesDtoResponse response = GlassesDtoResponse.builder().id(EXISTING_ID).brandName(BRAND_NAME_NEW).price(PRICE_ONE).build();
            when(glassesService.createGlasses(any(GlassesDtoRequest.class))).thenReturn(response);

            mockMvc.perform(post(BASE_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(EXISTING_ID.intValue()))
                    .andExpect(jsonPath("$.brandName").value(BRAND_NAME_NEW));
        }

        @Test
        @DisplayName("should return 400 when body is invalid")
        void createGlasses_withInvalidBody_returns400() throws Exception {
            GlassesDtoRequest invalidRequest = GlassesDtoRequest.builder().brandName("").price(null).build();

            mockMvc.perform(post(BASE_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(STATUS_BAD_REQUEST))
                    .andExpect(jsonPath("$.error").value("Bad Request"));
        }
    }

    @Nested
    @DisplayName("PUT update glass")
    class UpdateGlasses {

        @Test
        @DisplayName("should return 200 and updated DTO when glass exists")
        void updateGlasses_whenExists_returns200() throws Exception {
            GlassesDtoRequest request = GlassesDtoRequest.builder().brandName(BRAND_NAME_UPDATED).price(PRICE_TWO).build();
            GlassesDtoResponse response = GlassesDtoResponse.builder().id(EXISTING_ID).brandName(BRAND_NAME_UPDATED).price(PRICE_TWO).build();
            when(glassesService.updateGlasses(eq(EXISTING_ID), any(GlassesDtoRequest.class))).thenReturn(response);

            mockMvc.perform(put(BASE_PATH + "/" + EXISTING_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.brandName").value(BRAND_NAME_UPDATED));
        }

        @Test
        @DisplayName("should return 404 when glass does not exist")
        void updateGlasses_whenNotExists_returns404() throws Exception {
            GlassesDtoRequest request = GlassesDtoRequest.builder().brandName(BRAND_NAME_VALID).price(PRICE_ONE).build();
            when(glassesService.updateGlasses(eq(NOT_EXISTING_ID), any(GlassesDtoRequest.class)))
                    .thenThrow(new ResourceNotFoundException("Glass", NOT_EXISTING_ID));

            mockMvc.perform(put(BASE_PATH + "/" + NOT_EXISTING_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(STATUS_NOT_FOUND));
        }

        @Test
        @DisplayName("should return 400 when body is invalid")
        void updateGlasses_withInvalidBody_returns400() throws Exception {
            GlassesDtoRequest invalidRequest = GlassesDtoRequest.builder()
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
    @DisplayName("DELETE glass")
    class DeleteGlasses {

        @Test
        @DisplayName("should return 200 when glass is deleted")
        void deleteGlasses_returns200() throws Exception {
            mockMvc.perform(delete(BASE_PATH + "/" + EXISTING_ID))
                    .andExpect(status().isOk());
            verify(glassesService).deleteGlasses(EXISTING_ID);
        }
    }
}
