package com.lodecra.apiV1.dto;

public record LibroDto(String id, String name, String author, int price, String publisher, String contact, int stock, boolean discarded) {}
