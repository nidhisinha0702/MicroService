package codewithnidhi.product_service.dto;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public record ProductResponse(String id, String name,String description,BigDecimal price) {

}
