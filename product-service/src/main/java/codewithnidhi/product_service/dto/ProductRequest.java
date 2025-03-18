package codewithnidhi.product_service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public record ProductRequest(String name,String description,BigDecimal price) {

}
