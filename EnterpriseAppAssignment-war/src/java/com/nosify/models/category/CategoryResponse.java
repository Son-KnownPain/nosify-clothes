package com.nosify.models.category;

import com.nosify.models.product.ProductResponse;
import java.util.List;
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
public class CategoryResponse {
    private int categoryID;
    private String name;
    private String description;
    private String thumbnail;
    private List<ProductResponse> productList;
}
