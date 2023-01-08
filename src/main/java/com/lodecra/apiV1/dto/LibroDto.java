package com.lodecra.apiV1.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record LibroDto(@JsonInclude(JsonInclude.Include.NON_NULL) String code,
                       String name,
                       String author,
                       @JsonInclude(JsonInclude.Include.NON_NULL) Integer price,
                       String publisher,
                       String contact,
                       @JsonInclude(JsonInclude.Include.NON_NULL) Integer stock,
                       @JsonInclude(JsonInclude.Include.NON_NULL) Boolean discarded) {}
