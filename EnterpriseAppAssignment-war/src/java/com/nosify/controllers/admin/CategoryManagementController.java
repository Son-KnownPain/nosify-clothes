package com.nosify.controllers.admin;

import com.nosify.entities.Categories;
import com.nosify.models.category.AdminAddCategory;
import com.nosify.models.category.AdminDeleteCategory;
import com.nosify.models.category.AdminEditCategory;
import com.nosify.providers.AdminUrlProvider;
import com.nosify.session_beans.CategoriesFacadeLocal;
import com.nosify.session_beans.ProductsFacadeLocal;
import com.nosify.supports.FileSupport;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(AdminUrlProvider.ADMIN_PREFIX + AdminUrlProvider.Category.PREFIX + "")
public class CategoryManagementController {

    ProductsFacadeLocal productsFacade = lookupProductsFacadeLocal();

    CategoriesFacadeLocal categoriesFacade = lookupCategoriesFacadeLocal();
    
    @GetMapping({AdminUrlProvider.Category.INDEX1, AdminUrlProvider.Category.INDEX2, AdminUrlProvider.Category.INDEX3})
    public String index(Model model) {
        List<Categories> categories = categoriesFacade.findAll();
        model.addAttribute("categories", categories);

        return "/admin/category/index";
    }
    
    @GetMapping(AdminUrlProvider.Category.ADD + "")
    public ModelAndView add() {
        return new ModelAndView("/admin/category/add", "categoryAdd", new AdminAddCategory());
    }
    
    @PostMapping(AdminUrlProvider.Category.STORE + "")
    public String store(
            @ModelAttribute("categoryAdd") @Valid AdminAddCategory data, 
            BindingResult br,
            @RequestParam("thumbnailFile") MultipartFile thumbnailFile,
            HttpSession session
    ) {
        if (br.hasErrors()) return "/admin/category/add";
        
        Categories category = new Categories();
        category.setName(data.getName());
        category.setDescription(data.getDescription());
        
        if (thumbnailFile == null || thumbnailFile.isEmpty()) {
            category.setThumbnail(null);
        } else {
            try {
                byte[] thumbnailFileBytes = thumbnailFile.getBytes();
                String originFileName = thumbnailFile.getOriginalFilename();
                String fileName = FileSupport.saveFile(
                        session.getServletContext().getRealPath("/"),
                        "category",
                        thumbnailFileBytes,
                        originFileName
                );
                category.setThumbnail(fileName);
            } catch (IOException ex) {
                Logger.getLogger(ProductManagementController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        categoriesFacade.create(category);
        
        return "redirect:" + AdminUrlProvider.ADMIN_PREFIX + AdminUrlProvider.Category.PREFIX + AdminUrlProvider.Category.INDEX3;
    }
    
    @GetMapping(AdminUrlProvider.Category.EDIT + "")
    public ModelAndView edit(@PathVariable("id") int id) {
        Categories category = categoriesFacade.find(id);
        
        if (category == null) {
            return new ModelAndView("redirect:/admin/category");
        }
        
        AdminEditCategory categoryEdit = AdminEditCategory.builder()
                .categoryID(id)
                .name(category.getName())
                .description(category.getDescription())
                .thumbnail(category.getThumbnail())
                .build();
        
        return new ModelAndView("/admin/category/edit", "categoryEdit", categoryEdit);
    }
    
    @PutMapping(AdminUrlProvider.Category.UPDATE + "")
    public String update(
            @ModelAttribute("categoryEdit") @Valid AdminEditCategory data, 
            BindingResult br,
            @RequestParam("thumbnailFile") MultipartFile thumbnailFile,
            HttpSession session
    ) {
        if (br.hasErrors()) return "/admin/category/edit";
        
        Categories category = categoriesFacade.find(data.getCategoryID());
        
        if (category == null) {
            return "redirect:/admin/category";
        }
        
        category.setName(data.getName());
        category.setDescription(data.getDescription());
        
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            try {
                // Delete old file
                FileSupport.deleteFile(session.getServletContext().getRealPath("/"), "category", category.getThumbnail());
                
                // Save new file
                byte[] thumbnailFileBytes = thumbnailFile.getBytes();
                String originFileName = thumbnailFile.getOriginalFilename();
                String fileName = FileSupport.saveFile(
                        session.getServletContext().getRealPath("/"), 
                        "category", 
                        thumbnailFileBytes, 
                        originFileName
                );
                category.setThumbnail(fileName);
            } catch (IOException ex) {
                Logger.getLogger(ProductManagementController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        categoriesFacade.edit(category);
        
        return "redirect:/admin/category";
    }

    @GetMapping(AdminUrlProvider.Category.DELETE + "")
    public ModelAndView showDeleteConfirm(@PathVariable("id") int id) {
        Categories category = categoriesFacade.find(id);
        
        if (category == null) {
            return new ModelAndView("redirect:/admin/category");
        }
        
        AdminDeleteCategory categoryDelete = AdminDeleteCategory.builder()
                .categoryID(id)
                .build();
        
        return new ModelAndView("/admin/category/delete", "categoryDelete", categoryDelete);
    }
    
    @DeleteMapping(AdminUrlProvider.Category.DELETE_CONFIRMATION + "")
    public String delete(@ModelAttribute("categoryDelete") @Valid AdminDeleteCategory data, BindingResult br, HttpSession session) {
        if (br.hasErrors()) return "/admin/category";
        
        Categories category = categoriesFacade.find(data.getCategoryID());
        
        if (category == null) {
            return "redirect:/admin/category";
        }
        
        categoriesFacade.remove(category);
        
        try {
            FileSupport.deleteFile(session.getServletContext().getRealPath("/"), "category", category.getThumbnail());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return "redirect:/admin/category";
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
