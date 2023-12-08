package com.nosify.controllers.api;

import com.nosify.entities.Categories;
import com.nosify.entities.Products;
import com.nosify.models.category.CategoryResponse;
import com.nosify.models.product.ProductResponse;
import com.nosify.models.responses.DataResponse;
import com.nosify.providers.UrlProvider;
import com.nosify.session_beans.CategoriesFacadeLocal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlProvider.API_PREFIX + UrlProvider.Category.PREFIX)
public class CategoryApiController {

    CategoriesFacadeLocal categoriesFacade = lookupCategoriesFacadeLocal();
    
    @GetMapping("" + UrlProvider.Category.FOR_HOME_PAGE)
    public ResponseEntity<?> categoriesForHomePage() {
        List<Categories> categoriesWithThumbnail = categoriesFacade.getCategoriesesByThumbnail(true);
        List<CategoryResponse> data = new ArrayList<>();
        for (Categories c : categoriesWithThumbnail) {
            List<ProductResponse> productList = new ArrayList<>();
            int i = 1;
            for (Products product : c.getProductsCollection()) {
                productList.add(
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
                if (i++ == 6) {
                    break;
                }
            }
            data.add(
                    CategoryResponse.builder()
                            .categoryID(c.getCategoryID())
                            .name(c.getName())
                            .description(c.getDescription())
                            .thumbnail(c.getThumbnail())
                            .productList(productList)
                            .build()
            );
        }
        
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("" + UrlProvider.Category.WITHOUT_THUMBNAIL)
    public ResponseEntity<?> categoriesWithoutThumbnail() {
        List<Categories> categoriesWithoutThumbnail = categoriesFacade.getCategoriesesByThumbnail(false);
        List<CategoryResponse> data = new ArrayList<>();
        
        for (Categories c : categoriesWithoutThumbnail) {
            data.add(
                    CategoryResponse.builder()
                            .categoryID(c.getCategoryID())
                            .name(c.getName())
                            .description(c.getDescription())
                            .thumbnail(c.getThumbnail())
                            .productList(null)
                            .build()
            );
        }
        
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("" + UrlProvider.Category.GET_ALL)
    public ResponseEntity<?> getAll() {
        List<Categories> categories = categoriesFacade.findAll();
        List<CategoryResponse> data = new ArrayList<>();
        for (Categories c : categories) {
            List<ProductResponse> productList = new ArrayList<>();
            for (Products product : c.getProductsCollection()) {
                productList.add(
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
            data.add(
                CategoryResponse.builder()
                        .categoryID(c.getCategoryID())
                        .name(c.getName())
                        .description(c.getDescription())
                        .thumbnail(c.getThumbnail())
                        .productList(productList)
                        .build()
            );
        }
        
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("" + UrlProvider.Category.BY_ID)
    public ResponseEntity<?> byID(@PathVariable("id") int id) {
        Categories c = categoriesFacade.find(id);
        
        DataResponse<CategoryResponse> res = new DataResponse<>();
        
        if (c == null) {
            res.setSuccess(false);
            res.setMessage("Category was not found");
            res.setData(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
        
        List<ProductResponse> productList = new ArrayList<>();
        
        for (Products product : c.getProductsCollection()) {
            productList.add(
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
        
        CategoryResponse result = CategoryResponse.builder()
                .categoryID(id)
                .name(c.getName())
                .description(c.getDescription())
                .thumbnail(c.getThumbnail())
                .productList(productList)
                .build();
        
        res.setSuccess(true);
        res.setMessage("Successfully get category detail!");
        res.setData(result);
        
        return ResponseEntity.ok(res);
    }

    private CategoriesFacadeLocal lookupCategoriesFacadeLocal() {
        try {
            Context c = new InitialContext();
            return (CategoriesFacadeLocal) c.lookup("java:global/EnterpriseAppAssignment/EnterpriseAppAssignment-ejb/CategoriesFacade!com.nosify.session_beans.CategoriesFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
