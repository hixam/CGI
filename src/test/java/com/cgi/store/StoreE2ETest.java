package com.cgi.store;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.cgi.store.dto.request.FinsDtoRequest;
import com.cgi.store.dto.request.GlassesDtoRequest;
import com.cgi.store.dto.response.FinsDtoResponse;
import com.cgi.store.dto.response.GlassesDtoResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StoreE2ETest {

    private static final String FINS_BASE = "/products/fins";
    private static final String GLASSES_BASE = "/products/glasses";

    private static final String BRAND_NAME_CREATE = "E2E Fin";
    private static final String BRAND_NAME_UPDATE = "E2E Fin Updated";
    private static final BigDecimal PRICE_CREATE = BigDecimal.valueOf(99.99);
    private static final BigDecimal PRICE_UPDATE = BigDecimal.valueOf(49.99);

    private static final String GLASSES_BRAND_CREATE = "E2E Glass";
    private static final String GLASSES_BRAND_UPDATE = "E2E Glass Updated";
    private static final BigDecimal GLASSES_PRICE_CREATE = BigDecimal.valueOf(79.99);
    private static final BigDecimal GLASSES_PRICE_UPDATE = BigDecimal.valueOf(39.99);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Fins CRUD: create, get, update, get again, delete, get returns 404")
    void finsCrud_fullFlow_worksEndToEnd() {
        FinsDtoRequest createRequest = FinsDtoRequest.builder()
                .brandName(BRAND_NAME_CREATE)
                .price(PRICE_CREATE)
                .build();

        ResponseEntity<FinsDtoResponse> createResponse = restTemplate.postForEntity(FINS_BASE, createRequest, FinsDtoResponse.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().getId()).isNotNull();
        assertThat(createResponse.getBody().getBrandName()).isEqualTo(BRAND_NAME_CREATE);
        Long id = createResponse.getBody().getId();

        ResponseEntity<FinsDtoResponse> getResponse = restTemplate.getForEntity(FINS_BASE + "/" + id, FinsDtoResponse.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getBrandName()).isEqualTo(BRAND_NAME_CREATE);

        FinsDtoRequest updateRequest = FinsDtoRequest.builder()
                .brandName(BRAND_NAME_UPDATE)
                .price(PRICE_UPDATE)
                .build();

        ResponseEntity<FinsDtoResponse> updateResponse = restTemplate.exchange(
                FINS_BASE + "/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                FinsDtoResponse.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody()).isNotNull();
        assertThat(updateResponse.getBody().getBrandName()).isEqualTo(BRAND_NAME_UPDATE);

        ResponseEntity<FinsDtoResponse> getAfterUpdate = restTemplate.getForEntity(FINS_BASE + "/" + id, FinsDtoResponse.class);
        assertThat(getAfterUpdate.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getAfterUpdate.getBody().getBrandName()).isEqualTo(BRAND_NAME_UPDATE);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(FINS_BASE + "/" + id, HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getAfterDelete = restTemplate.getForEntity(FINS_BASE + "/" + id, String.class);
        assertThat(getAfterDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Glasses CRUD: create, get, update, get again, delete, get returns 404")
    void glassesCrud_fullFlow_worksEndToEnd() {
        GlassesDtoRequest createRequest = GlassesDtoRequest.builder()
                .brandName(GLASSES_BRAND_CREATE)
                .price(GLASSES_PRICE_CREATE)
                .build();

        ResponseEntity<GlassesDtoResponse> createResponse = restTemplate.postForEntity(GLASSES_BASE, createRequest, GlassesDtoResponse.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().getId()).isNotNull();
        assertThat(createResponse.getBody().getBrandName()).isEqualTo(GLASSES_BRAND_CREATE);
        Long id = createResponse.getBody().getId();

        ResponseEntity<GlassesDtoResponse> getResponse = restTemplate.getForEntity(GLASSES_BASE + "/" + id, GlassesDtoResponse.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getBrandName()).isEqualTo(GLASSES_BRAND_CREATE);

        GlassesDtoRequest updateRequest = GlassesDtoRequest.builder()
                .brandName(GLASSES_BRAND_UPDATE)
                .price(GLASSES_PRICE_UPDATE)
                .build();

        ResponseEntity<GlassesDtoResponse> updateResponse = restTemplate.exchange(
                GLASSES_BASE + "/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                GlassesDtoResponse.class);
                
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody()).isNotNull();
        assertThat(updateResponse.getBody().getBrandName()).isEqualTo(GLASSES_BRAND_UPDATE);

        ResponseEntity<GlassesDtoResponse> getAfterUpdate = restTemplate.getForEntity(GLASSES_BASE + "/" + id, GlassesDtoResponse.class);
        assertThat(getAfterUpdate.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getAfterUpdate.getBody().getBrandName()).isEqualTo(GLASSES_BRAND_UPDATE);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(GLASSES_BASE + "/" + id, HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getAfterDelete = restTemplate.getForEntity(GLASSES_BASE + "/" + id, String.class);
        assertThat(getAfterDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
