package com.example.yamabar2.DTO;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String type;
    private String name;
    private String price;
    private String description;
    private Long imageId;
}
