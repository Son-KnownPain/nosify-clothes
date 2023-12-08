package com.nosify.models.order;

import com.nosify.entities.Users;
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
public class AdminShowOrder {
    private Integer orderID;
    private String address;
    private String phone;
    private String orderDate;
    private String deliveriedDate;
    private String status;
    private int totalPrice;
    private Users userID;
    private List<ProductsOfOrder> productsOfOrder;
}
