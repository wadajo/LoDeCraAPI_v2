package com.lodecra.apiV1.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Positive;

public record BookDto(
        @JsonInclude(JsonInclude.Include.NON_NULL) String code,
       String name,
       String author,
       @Positive(message = "Price must be greater than zero.") @JsonInclude(JsonInclude.Include.NON_NULL) Integer price,
       String publisher,
       String contact,
       @JsonInclude(JsonInclude.Include.NON_NULL) Integer stock,
       @JsonInclude(JsonInclude.Include.NON_NULL) Boolean discarded) {}
