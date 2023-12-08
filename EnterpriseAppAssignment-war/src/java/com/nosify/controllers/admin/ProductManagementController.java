package com.nosify.controllers.admin;

import com.nosify.entities.Categories;
import com.nosify.entities.Products;
import com.nosify.models.common.AdminDeleteRecord;
import com.nosify.models.product.AdminAddProduct;
import com.nosify.models.product.AdminEditProduct;
import com.nosify.providers.AdminUrlProvider;
import com.nosify.session_beans.CategoriesFacadeLocal;
import com.nosify.session_beans.ProductsFacadeLocal;
import com.nosify.supports.FileSupport;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(AdminUrlProvider.ADMIN_PREFIX + AdminUrlProvider.Product.PREFIX + "")
@SessionAttributes("categoriesSelect")
public class ProductManagementController {

    CategoriesFacadeLocal categoriesFacade = lookupCategoriesFacadeLocal();

    ProductsFacadeLocal productsFacade = lookupProductsFacadeLocal();
    
    @GetMapping({AdminUrlProvider.Product.INDEX1, AdminUrlProvider.Product.INDEX2, AdminUrlProvider.Product.INDEX3})
    public String index(@RequestParam(name = "q", required = false) String q, Model model) {
        List<Products> data;
        
        if (q == null) {
            data = productsFacade.findAll();
        } else {
            data = productsFacade.findLikeName(q);
        }
        
        Collections.reverse(data);
        
        model.addAttribute("data", data);
        
        return "/admin/product/index";
    }
    
    @GetMapping(AdminUrlProvider.Product.ADD + "")
    public ModelAndView add(Model model) {
        // This list for form select
        List<Categories> categoriesSelect = categoriesFacade.findAll();
        model.addAttribute("categoriesSelect", categoriesSelect);
        
        return new ModelAndView("/admin/product/add", "productAdd", new AdminAddProduct());
    }
    
    @PostMapping(AdminUrlProvider.Product.STORE + "")
    public String store(
            @ModelAttribute("productAdd") @Valid AdminAddProduct data, 
            BindingResult br,
            @RequestParam("thumbnailFile") MultipartFile thumbnailFile,
            HttpSession session
    ) {
        if (thumbnailFile == null || thumbnailFile.isEmpty()) {
            br.rejectValue("thumbnail", "error.thumbnail", "Thumbnail file must be uploaded");
        }
        
        Categories category = categoriesFacade.find(data.getCategoryID());
        if (category == null) {
            br.rejectValue("categoryID", "error.categoryID", "Category ID invalid");
        }
        
        if (br.hasErrors()) return "/admin/product/add";
        
        Products product = new Products();
        
        product.setCategoryID(category);
        product.setDescription(data.getDescription());
        product.setDiscount((double) data.getDiscount() / 100);
        product.setName(data.getName());
        product.setPrice(data.getPrice());
        product.setQuantityInStock(data.getQuantityInStock());
        
        try {
            byte[] thumbnailFileBytes = thumbnailFile.getBytes();
            String originFileName = thumbnailFile.getOriginalFilename();
            String fileName = FileSupport.saveFile(
                    session.getServletContext().getRealPath("/"), 
                    "product", 
                    thumbnailFileBytes, 
                    originFileName
            );
            product.setThumbnail(fileName);
        } catch (IOException ex) {
            Logger.getLogger(ProductManagementController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        productsFacade.create(product);
        
        return "redirect:/admin/product";
    }
    
    @GetMapping(AdminUrlProvider.Category.EDIT + "")
    public ModelAndView edit(@PathVariable("id") int id, Model model) {
        Products product = productsFacade.find(id);
        
        if (product == null) {
            return new ModelAndView("redirect:/admin/product");
        }
        
        // This list for form select
        List<Categories> categoriesSelect = categoriesFacade.findAll();
        model.addAttribute("categoriesSelect", categoriesSelect);
        
        AdminEditProduct productEdit = AdminEditProduct.builder()
                .productID(id)
                .name(product.getName())
                .thumbnail(product.getThumbnail())
                .categoryID(product.getCategoryID().getCategoryID())
                .discount((int) (product.getDiscount() * 100))
                .price((int) product.getPrice())
                .quantityInStock(product.getQuantityInStock())
                .description(product.getDescription())
                .build();
        
        return new ModelAndView("/admin/product/edit", "productEdit", productEdit);
    }
    
    @PutMapping(AdminUrlProvider.Product.UPDATE + "")
    public String update(
            @ModelAttribute("productEdit") @Valid AdminEditProduct data, 
            BindingResult br,
            @RequestParam("thumbnailFile") MultipartFile thumbnailFile,
            HttpSession session
    ) {
        if (br.hasErrors()) return "/admin/product/edit";
        
        Products product = productsFacade.find(data.getProductID());
        
        Categories category = categoriesFacade.find(data.getCategoryID());
                
        if (product == null || category == null) {
            return "redirect:/admin/product";
        }
        
        product.setCategoryID(category);
        product.setDescription(data.getDescription());
        product.setDiscount((double) data.getDiscount() / 100);
        product.setName(data.getName());
        product.setPrice(data.getPrice());
        product.setQuantityInStock(data.getQuantityInStock());
        
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            try {
                // Delete old file
                FileSupport.deleteFile(session.getServletContext().getRealPath("/"), "product", product.getThumbnail());
                
                // Save new file
                byte[] thumbnailFileBytes = thumbnailFile.getBytes();
                String originFileName = thumbnailFile.getOriginalFilename();
                String fileName = FileSupport.saveFile(
                        session.getServletContext().getRealPath("/"), 
                        "product", 
                        thumbnailFileBytes, 
                        originFileName
                );
                product.setThumbnail(fileName);
            } catch (IOException ex) {
                Logger.getLogger(ProductManagementController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        productsFacade.edit(product);
        
        return "redirect:/admin/product";
    }
    
    @GetMapping(AdminUrlProvider.Product.DELETE + "")
    public ModelAndView showDeleteConfirm(@PathVariable("id") int id) {
        Products product = productsFacade.find(id);
        
        if (product == null) return new ModelAndView("redirect:/admin/product");
        
        AdminDeleteRecord idObject = AdminDeleteRecord.builder().id(id).build();
        
        return new ModelAndView("/admin/product/delete", "productDelete", idObject);
    }
    
    @DeleteMapping(AdminUrlProvider.Product.DELETE_CONFIRMATION + "")
    public String delete(@ModelAttribute("productDelete") @Valid AdminDeleteRecord data, BindingResult br, HttpSession session) {
        if (br.hasErrors()) return "/admin/product/delete";
        
        Products product = productsFacade.find(data.getId());
        
        if (product == null) return "redirect:/admin/product";
        
        productsFacade.remove(product);
        
        try {
            FileSupport.deleteFile(session.getServletContext().getRealPath("/"), "product", product.getThumbnail());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return "redirect:/admin/product";
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
