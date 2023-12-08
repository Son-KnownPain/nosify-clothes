package com.nosify.models.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailResponse {
    private int productID;
    private String name;
    private double price;
    private double discount;
    private String description;
    private int quantityInStock;
    private String thumbnail;
    private int categoryID;
    private String categoryName;
}
