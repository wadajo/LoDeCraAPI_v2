package com.lodecra.apiV1.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;

public record BookDto(

        @Pattern(regexp = "^\\d{2}_\\w{5}$", message = "Book code doesn't have the correct format.")@JsonInclude(JsonInclude.Include.NON_NULL) String code,
        @NotBlank(message = "Book must have a name.") String name,
        @NotBlank(message = "Book must have an author.") String author,
        @Positive(message = "Price must be greater than zero.") @JsonInclude(JsonInclude.Include.NON_NULL) Integer price,
        String publisher,
        String contact,
        @JsonInclude(JsonInclude.Include.NON_NULL) Integer stock,
        @JsonInclude(JsonInclude.Include.NON_NULL) Boolean discarded) {}
