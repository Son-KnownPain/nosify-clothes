package com.nosify.controllers.api;

import com.nosify.entities.Products;
import com.nosify.exceptions.RecordNotFoundException;
import com.nosify.models.product.ProductDetailResponse;
import com.nosify.models.product.ProductResponse;
import com.nosify.models.responses.DataResponse;
import com.nosify.providers.UrlProvider;
import com.nosify.session_beans.ProductsFacadeLocal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlProvider.API_PREFIX + UrlProvider.Product.PREFIX)
public class ProductApiController {

    ProductsFacadeLocal productsFacade = lookupProductsFacadeLocal();
    
    @GetMapping("" + UrlProvider.Product.BY_NAME)
    public ResponseEntity<List<ProductDetailResponse>> byName(@RequestParam("name") String name) {
        List<Products> products = productsFacade.findLikeName(name);
        
        List<ProductDetailResponse> data = new ArrayList<>();
        for (Products product : products) {
            data.add(
                    ProductDetailResponse.builder()
                        .productID(product.getProductID())
                        .name(product.getName())
                        .price(product.getPrice())
                        .discount(product.getDiscount())
                        .description(product.getDescription())
                        .quantityInStock(product.getQuantityInStock())
                        .thumbnail(product.getThumbnail())
                        .categoryID(product.getCategoryID().getCategoryID())
                        .categoryName(product.getCategoryID().getName())
                        .build()
            );
        }
        
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("" + UrlProvider.Product.DETAIL)
    public ResponseEntity<DataResponse<ProductDetailResponse>> detail(@PathVariable("id") int id) {
        Products product = productsFacade.find(id);
        
        if (product == null) {
            throw new RecordNotFoundException("Record not found!");
        }
        
        DataResponse<ProductDetailResponse> res = new DataResponse<>();
        res.setSuccess(true);
        res.setMessage("Successfully query data");
        res.setData(
            ProductDetailResponse.builder()
                    .productID(product.getProductID())
                    .name(product.getName())
                    .price(product.getPrice())
                    .discount(product.getDiscount())
                    .description(product.getDescription())
                    .quantityInStock(product.getQuantityInStock())
                    .thumbnail(product.getThumbnail())
                    .categoryID(product.getCategoryID().getCategoryID())
                    .categoryName(product.getCategoryID().getName())
                    .build()
        );
        
        return ResponseEntity.ok(res);
    }
    
    @GetMapping("" + UrlProvider.Product.BY_LIST_ID)
    public ResponseEntity<?> byListID(@RequestParam("ids") String ids) {
        List<Integer> idsInt = new ArrayList<>();
        
        if (ids != null) {
            StringTokenizer tokenizer = new StringTokenizer(ids, ",");
            while (tokenizer.hasMoreElements()) {
                String idString = (String) tokenizer.nextElement();
                try {
                    int id = Integer.parseInt(idString.trim());
                    idsInt.add(id);
                } catch (NumberFormatException e) {

                }
            }
        }
        
        List<ProductResponse> data = new ArrayList<>();
        
        if (!idsInt.isEmpty()) {
            List<Products> products = productsFacade.findByListID(idsInt);
        
            for (Products product : products) {
                data.add(
                        ProductResponse.builder()
                            .productID(product.getProductID())
                            .name(product.getName())
                            .price(product.getPrice())
                            .discount(product.getDiscount())
                            .description(product.getDescription())
                            .quantityInStock(product.getQuantityInStock())
                            .thumbnail(product.getThumbnail())
                            .build()
                );
            }
        }
        
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("" + UrlProvider.Product.RELATED_PRODUCTS)
    public ResponseEntity<?> getRelatedProducts(@PathVariable("id") int id) {
        List<Products> products = productsFacade.findRelatedProducts(id);
        
        List<ProductResponse> data = new ArrayList<>();
        
        for (Products product : products) {
            data.add(
                ProductResponse.builder()
                    .productID(product.getProductID())
                    .name(product.getName())
                    .price(product.getPrice())
                    .discount(product.getDiscount())
                    .description(product.getDescription())
                    .quantityInStock(product.getQuantityInStock())
                    .thumbnail(product.getThumbnail())
                    .build()
            );
        }
        
        return ResponseEntity.ok(data);
    }

    private ProductsFacadeLocal lookupProductsFacadeLocal() {
        try {
            Context c = new InitialContext();
            return (ProductsFacadeLocal) c.lookup("java:global/EnterpriseAppAssignment/EnterpriseAppAssignment-ejb/ProductsFacade!com.nosify.session_beans.ProductsFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}