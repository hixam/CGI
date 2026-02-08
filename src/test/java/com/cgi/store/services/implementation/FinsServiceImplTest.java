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

import com.cgi.store.dto.request.FinsDtoRequest;
import com.cgi.store.dto.response.FinsDtoResponse;
import com.cgi.store.entities.FinsEntity;
import com.cgi.store.exceptions.ResourceNotFoundException;
import com.cgi.store.mapper.FinsMapper;
import com.cgi.store.repository.FinsRepository;

@ExtendWith(MockitoExtension.class)
class FinsServiceImplTest {

    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 999L;
    private static final String BRAND_NAME_TEST = "Test";
    private static final String BRAND_NAME_NEW = "New";
    private static final String BRAND_NAME_OLD = "Old";
    private static final String BRAND_NAME_UPDATED = "Updated";
    private static final String BRAND_NAME_ANY = "X";
    private static final String RESOURCE_NAME = "Fin";
    private static final BigDecimal PRICE_TEN = BigDecimal.TEN;
    private static final BigDecimal PRICE_ONE = BigDecimal.ONE;
    private static final BigDecimal PRICE_TWO = BigDecimal.valueOf(2);

    @Mock
    private FinsRepository finsRepository;

    private FinsServiceImpl finsService;

    @BeforeEach
    void setUp() {
        finsService = new FinsServiceImpl(finsRepository, FinsMapper.INSTANCE);
    }

    @Nested
    @DisplayName("Get all fins")
    class GetAllFins {

        @Test
        @DisplayName("should return mapped list when fins exist")
        void getAllFins_returnsMappedList() {
            FinsEntity entity = FinsEntity.builder().id(EXISTING_ID).brandName(BRAND_NAME_TEST).price(PRICE_TEN).build();
            when(finsRepository.findAll()).thenReturn(List.of(entity));

            List<FinsDtoResponse> result = finsService.getAllFins();

            assertThat(result).hasSize(1);
            assertThat(result.getFirst().getId()).isEqualTo(EXISTING_ID);
            assertThat(result.getFirst().getBrandName()).isEqualTo(BRAND_NAME_TEST);
            assertThat(result.getFirst().getPrice()).isEqualByComparingTo(PRICE_TEN);
        }
    }

    @Nested
    @DisplayName("Get fin by ID")
    class GetFinsById {

        @Test
        @DisplayName("should return DTO when fin exists")
        void getFinsById_whenExists_returnsDto() {
            FinsEntity entity = FinsEntity.builder().id(EXISTING_ID).brandName(BRAND_NAME_TEST).price(PRICE_TEN).build();
            when(finsRepository.findById(EXISTING_ID)).thenReturn(Optional.of(entity));

            FinsDtoResponse result = finsService.getFinsById(EXISTING_ID);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(EXISTING_ID);
            assertThat(result.getBrandName()).isEqualTo(BRAND_NAME_TEST);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when fin does not exist")
        void getFinsById_whenNotExists_throwsResourceNotFoundException() {
            when(finsRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> finsService.getFinsById(NOT_EXISTING_ID))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage(RESOURCE_NAME + " not found with id: " + NOT_EXISTING_ID);
        }
    }

    @Nested
    @DisplayName("Create fin")
    class CreateFins {

        @Test
        @DisplayName("should return saved DTO when request is valid")
        void createFins_returnsSavedDto() {
            FinsDtoRequest request = FinsDtoRequest.builder().brandName(BRAND_NAME_NEW).price(PRICE_ONE).build();
            FinsEntity savedEntity = FinsEntity.builder().id(EXISTING_ID).brandName(BRAND_NAME_NEW).price(PRICE_ONE).build();
            when(finsRepository.save(any(FinsEntity.class))).thenReturn(savedEntity);

            FinsDtoResponse result = finsService.createFins(request);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(EXISTING_ID);
            assertThat(result.getBrandName()).isEqualTo(BRAND_NAME_NEW);
        }
    }

    @Nested
    @DisplayName("Update fin")
    class UpdateFins {

        @Test
        @DisplayName("should return updated DTO when fin exists")
        void updateFins_whenExists_returnsUpdatedDto() {
            FinsEntity existing = FinsEntity.builder().id(EXISTING_ID).brandName(BRAND_NAME_OLD).price(PRICE_ONE).build();
            FinsDtoRequest request = FinsDtoRequest.builder().brandName(BRAND_NAME_UPDATED).price(PRICE_TWO).build();
            when(finsRepository.findById(EXISTING_ID)).thenReturn(Optional.of(existing));
            when(finsRepository.save(any(FinsEntity.class))).thenAnswer(inv -> inv.getArgument(0));

            FinsDtoResponse result = finsService.updateFins(EXISTING_ID, request);

            assertThat(result).isNotNull();
            assertThat(result.getBrandName()).isEqualTo(BRAND_NAME_UPDATED);
            assertThat(result.getPrice()).isEqualByComparingTo(PRICE_TWO);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when fin does not exist")
        void updateFins_whenNotExists_throwsResourceNotFoundException() {
            FinsDtoRequest request = FinsDtoRequest.builder().brandName(BRAND_NAME_ANY).price(PRICE_ONE).build();
            when(finsRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> finsService.updateFins(NOT_EXISTING_ID, request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage(RESOURCE_NAME + " not found with id: " + NOT_EXISTING_ID);
        }
    }

    @Nested
    @DisplayName("Delete fin")
    class DeleteFins {

        @Test
        @DisplayName("should call repository deleteById when fin is deleted")
        void deleteFins_callsRepositoryDeleteById() {
            finsService.deleteFins(EXISTING_ID);
            verify(finsRepository).deleteById(EXISTING_ID);
        }
    }
}
