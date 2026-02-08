package com.cgi.store.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.cgi.store.entities.FinsEntity;

@DataJpaTest
@ActiveProfiles("test")
class FinsRepositoryTest {

    private static final Long NOT_EXISTING_ID = 999L;
    private static final String BRAND_NAME_TEST = "Test";
    private static final String BRAND_NAME_TO_DELETE = "ToDelete";
    private static final String BRAND_NAME_A = "A";
    private static final String BRAND_NAME_B = "B";
    private static final BigDecimal PRICE_TEN = BigDecimal.TEN;
    private static final BigDecimal PRICE_ONE = BigDecimal.ONE;

    @Autowired
    private FinsRepository finsRepository;

    @Test
    @DisplayName("should return entity when saved and found by id")
    void save_andFindById_returnsEntity() {
        FinsEntity entity = FinsEntity.builder().brandName(BRAND_NAME_TEST).price(PRICE_TEN).build();
        FinsEntity saved = finsRepository.save(entity);

        Optional<FinsEntity> found = finsRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getBrandName()).isEqualTo(BRAND_NAME_TEST);
        assertThat(found.get().getPrice()).isEqualByComparingTo(PRICE_TEN);
    }

    @Test
    @DisplayName("should return empty when fin does not exist")
    void findById_whenNotExists_returnsEmpty() {
        Optional<FinsEntity> found = finsRepository.findById(NOT_EXISTING_ID);
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("should remove entity when deleteById is called")
    void deleteById_removesEntity() {
        FinsEntity entity = FinsEntity.builder().brandName(BRAND_NAME_TO_DELETE).price(PRICE_ONE).build();
        FinsEntity saved = finsRepository.save(entity);
        Long id = saved.getId();

        finsRepository.deleteById(id);

        assertThat(finsRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("should return all saved entities when findAll is called")
    void findAll_returnsAllSaved() {
        finsRepository.save(FinsEntity.builder().brandName(BRAND_NAME_A).price(PRICE_ONE).build());
        finsRepository.save(FinsEntity.builder().brandName(BRAND_NAME_B).price(PRICE_TEN).build());

        assertThat(finsRepository.findAll()).hasSize(2);
    }
}
