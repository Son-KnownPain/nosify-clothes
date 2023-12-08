package com.nosify.models.product;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminEditProduct {
    @NotNull
    private int productID;
    
    private String thumbnail;
    
    @NotNull
    private int categoryID;
    
    @NotNull
    @NotEmpty
    private String name;
    
    @NotNull
    @Min(value = 0, message = "Price must be greather than 0")
    private int price;
    
    @NotNull
    @Min(value = 0, message = "Min value is 0")
    @Max(value = 100, message = "Max value is 100")
    private int discount;
    
    @NotNull
    @NotEmpty
    private String description;
    
    @NotNull
    @Min(value = 0, message = "Min quantity is 0")
    @Max(value = 999999999, message = "Max quantity is 999999999")
    private int quantityInStock;
}
