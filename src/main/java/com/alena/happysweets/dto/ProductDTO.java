package com.alena.happysweets.dto;

import lombok.Data;
//DTO class to separate product related data transfer concerns from business logic concerns
@Data
public class ProductDTO {
    private Long id;
    private String name;
    private int categoryId;
    private double price;
    private double weight;
    private String description;
    private String imageName;
}
