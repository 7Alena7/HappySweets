package com.alena.happysweets.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private int categoryId;
    private double price;
    private double weight;
    private String description;
    private String imageName;
    private String priceWeightRatio;

    public ProductDTO(String name, String priceWeightRatio) {
        this.name = name;
        this.priceWeightRatio = priceWeightRatio;
    }
}