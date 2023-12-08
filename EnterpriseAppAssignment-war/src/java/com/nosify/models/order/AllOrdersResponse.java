package com.nosify.models.order;

import java.util.Date;
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
public class AllOrdersResponse {
    private int orderID;
    private String phone;
    private String address;
    private Date orderDate;
    private Date deliveriedDate;
    private String status;
    private int totalPrice;
    private boolean ableToCancel;
    private boolean canceled;
    private List<ProductsOfOrder> productsOfOrder;
}
