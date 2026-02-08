package com.cgi.store.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.cgi.store.entities.GlassesEntity;

@DataJpaTest
@ActiveProfiles("test")
class GlassesRepositoryTest {

    private static final Long NOT_EXISTING_ID = 999L;
    private static final String BRAND_NAME_TEST = "Test";
    private static final String BRAND_NAME_TO_DELETE = "ToDelete";
    private static final String BRAND_NAME_A = "A";
    private static final String BRAND_NAME_B = "B";
    private static final BigDecimal PRICE_TEN = BigDecimal.TEN;
    private static final BigDecimal PRICE_ONE = BigDecimal.ONE;

    @Autowired
    private GlassesRepository glassesRepository;

    @Test
    @DisplayName("should return entity when saved and found by id")
    void save_andFindById_returnsEntity() {
        GlassesEntity entity = GlassesEntity.builder().brandName(BRAND_NAME_TEST).price(PRICE_TEN).build();
        GlassesEntity saved = glassesRepository.save(entity);

        Optional<GlassesEntity> found = glassesRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getBrandName()).isEqualTo(BRAND_NAME_TEST);
        assertThat(found.get().getPrice()).isEqualByComparingTo(PRICE_TEN);
    }

    @Test
    @DisplayName("should return empty when glass does not exist")
    void findById_whenNotExists_returnsEmpty() {
        Optional<GlassesEntity> found = glassesRepository.findById(NOT_EXISTING_ID);
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("should remove entity when deleteById is called")
    void deleteById_removesEntity() {
        GlassesEntity entity = GlassesEntity.builder().brandName(BRAND_NAME_TO_DELETE).price(PRICE_ONE).build();
        GlassesEntity saved = glassesRepository.save(entity);
        Long id = saved.getId();

        glassesRepository.deleteById(id);

        assertThat(glassesRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("should return all saved entities when findAll is called")
    void findAll_returnsAllSaved() {
        glassesRepository.save(GlassesEntity.builder().brandName(BRAND_NAME_A).price(PRICE_ONE).build());
        glassesRepository.save(GlassesEntity.builder().brandName(BRAND_NAME_B).price(PRICE_TEN).build());

        assertThat(glassesRepository.findAll()).hasSize(2);
    }
}
