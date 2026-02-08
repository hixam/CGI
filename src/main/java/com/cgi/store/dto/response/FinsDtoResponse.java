package com.cgi.store.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinsDtoResponse {
    private Long id;
    private String brandName;
    private BigDecimal price;
}
