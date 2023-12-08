package com.nosify.models.order;

import java.util.HashMap;
import java.util.List;
import javax.validation.constraints.Pattern;
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
public class UserOrderRequest {
    
    @NotEmpty(message = "Phone cannot empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 numbers")
    private String phone;
    
    @NotEmpty(message = "Address cannot empty")
    private String address;
    
    @NotEmpty(message = "Products cannot empty")
    private List<HashMap<String, Integer>> productsData;
}
