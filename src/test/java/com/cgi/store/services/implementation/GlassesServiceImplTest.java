package com.cgi.store.services.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cgi.store.dto.request.GlassesDtoRequest;
import com.cgi.store.dto.response.GlassesDtoResponse;
import com.cgi.store.entities.GlassesEntity;
import com.cgi.store.exceptions.ResourceNotFoundException;
import com.cgi.store.mapper.GlassesMapper;
import com.cgi.store.repository.GlassesRepository;

@ExtendWith(MockitoExtension.class)
class GlassesServiceImplTest {

    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 999L;
    private static final String BRAND_NAME_TEST = "Test";
    private static final String BRAND_NAME_NEW = "New";
    private static final String BRAND_NAME_OLD = "Old";
    private static final String BRAND_NAME_UPDATED = "Updated";
    private static final String BRAND_NAME_ANY = "X";
    private static final String RESOURCE_NAME = "Glass";
    private static final BigDecimal PRICE_TEN = BigDecimal.TEN;
    private static final BigDecimal PRICE_ONE = BigDecimal.ONE;
    private static final BigDecimal PRICE_TWO = BigDecimal.valueOf(2);

    @Mock
    private GlassesRepository glassesRepository;

    private GlassesServiceImpl glassesService;

    @BeforeEach
    void setUp() {
        glassesService = new GlassesServiceImpl(glassesRepository, GlassesMapper.INSTANCE);
    }

    @Nested
    @DisplayName("Get all glasses")
    class GetAllGlasses {

        @Test
        @DisplayName("should return mapped list when glasses exist")
        void getAllGlasses_returnsMappedList() {
            GlassesEntity entity = GlassesEntity.builder().id(EXISTING_ID).brandName(BRAND_NAME_TEST).price(PRICE_TEN).build();
            when(glassesRepository.findAll()).thenReturn(List.of(entity));

            List<GlassesDtoResponse> result = glassesService.getAllGlasses();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(EXISTING_ID);
            assertThat(result.get(0).getBrandName()).isEqualTo(BRAND_NAME_TEST);
        }
    }

    @Nested
    @DisplayName("Get glass by ID")
    class GetGlassesById {

        @Test
        @DisplayName("should return DTO when glass exists")
        void getGlassesById_whenExists_returnsDto() {
            GlassesEntity entity = GlassesEntity.builder().id(EXISTING_ID).brandName(BRAND_NAME_TEST).price(PRICE_TEN).build();
            when(glassesRepository.findById(EXISTING_ID)).thenReturn(Optional.of(entity));

            GlassesDtoResponse result = glassesService.getGlassesById(EXISTING_ID);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(EXISTING_ID);
            assertThat(result.getBrandName()).isEqualTo(BRAND_NAME_TEST);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when glass does not exist")
        void getGlassesById_whenNotExists_throwsResourceNotFoundException() {
            when(glassesRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> glassesService.getGlassesById(NOT_EXISTING_ID))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage(RESOURCE_NAME + " not found with id: " + NOT_EXISTING_ID);
        }
    }

    @Nested
    @DisplayName("Create glass")
    class CreateGlasses {

        @Test
        @DisplayName("should return saved DTO when request is valid")
        void createGlasses_returnsSavedDto() {
            GlassesDtoRequest request = GlassesDtoRequest.builder().brandName(BRAND_NAME_NEW).price(PRICE_ONE).build();
            GlassesEntity savedEntity = GlassesEntity.builder().id(EXISTING_ID).brandName(BRAND_NAME_NEW).price(PRICE_ONE).build();
            when(glassesRepository.save(any(GlassesEntity.class))).thenReturn(savedEntity);

            GlassesDtoResponse result = glassesService.createGlasses(request);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(EXISTING_ID);
            assertThat(result.getBrandName()).isEqualTo(BRAND_NAME_NEW);
        }
    }

    @Nested
    @DisplayName("Update glass")
    class UpdateGlasses {

        @Test
        @DisplayName("should return updated DTO when glass exists")
        void updateGlasses_whenExists_returnsUpdatedDto() {
            GlassesEntity existing = GlassesEntity.builder().id(EXISTING_ID).brandName(BRAND_NAME_OLD).price(PRICE_ONE).build();
            GlassesDtoRequest request = GlassesDtoRequest.builder().brandName(BRAND_NAME_UPDATED).price(PRICE_TWO).build();
            when(glassesRepository.findById(EXISTING_ID)).thenReturn(Optional.of(existing));
            when(glassesRepository.save(any(GlassesEntity.class))).thenAnswer(inv -> inv.getArgument(0));

            GlassesDtoResponse result = glassesService.updateGlasses(EXISTING_ID, request);

            assertThat(result).isNotNull();
            assertThat(result.getBrandName()).isEqualTo(BRAND_NAME_UPDATED);
            assertThat(result.getPrice()).isEqualByComparingTo(PRICE_TWO);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when glass does not exist")
        void updateGlasses_whenNotExists_throwsResourceNotFoundException() {
            GlassesDtoRequest request = GlassesDtoRequest.builder().brandName(BRAND_NAME_ANY).price(PRICE_ONE).build();
            when(glassesRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> glassesService.updateGlasses(NOT_EXISTING_ID, request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage(RESOURCE_NAME + " not found with id: " + NOT_EXISTING_ID);
        }
    }

    @Nested
    @DisplayName("Delete glass")
    class DeleteGlasses {

        @Test
        @DisplayName("should call repository deleteById when glass is deleted")
        void deleteGlasses_callsRepositoryDeleteById() {
            glassesService.deleteGlasses(EXISTING_ID);
            verify(glassesRepository).deleteById(EXISTING_ID);
        }
    }
}
