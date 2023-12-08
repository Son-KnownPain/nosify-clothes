package com.nosify.models.order;

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
public class ProductsOfOrder {
    private int productID;
    private String name;
    private double price;
    private double discount;
    private int quantityInStock;
    private int orderQuantity;
    private double orderUnitPrice;
    private String thumbnail;
}
