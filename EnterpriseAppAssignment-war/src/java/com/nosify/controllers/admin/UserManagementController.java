package com.nosify.controllers.admin;

import com.nosify.entities.Users;
import com.nosify.enums.AppConstants;
import com.nosify.enums.UserRoles;
import com.nosify.models.common.AdminDeleteRecord;
import com.nosify.models.user.AdminAddUser;
import com.nosify.models.user.AdminEditUser;
import com.nosify.providers.AdminUrlProvider;
import com.nosify.session_beans.UsersFacadeLocal;
import com.nosify.supports.FileSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@RequestMapping(AdminUrlProvider.ADMIN_PREFIX + AdminUrlProvider.User.PREFIX)
@SessionAttributes("roles")
public class UserManagementController {

    UsersFacadeLocal usersFacade = lookupUsersFacadeLocal();

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @GetMapping({AdminUrlProvider.User.INDEX1, AdminUrlProvider.User.INDEX2,  AdminUrlProvider.User.INDEX3})
    public ModelAndView index(
            @RequestParam(name = "q", required = false) String q
    ) {
        List<Users> data;
        
        if (q == null) {
            data = usersFacade.findAll();
        } else {
            data = usersFacade.findLikeName(q);
        }
        
        return new ModelAndView("/admin/user/index", "data", data);
    }
    
    @GetMapping("" +  AdminUrlProvider.User.ADD)
    public ModelAndView add(Model model) {
        
        
        model.addAttribute("roles", getRoleList());
        
        return new ModelAndView("/admin/user/add", "userAdd", new AdminAddUser());
    }
    
    @PostMapping("" + AdminUrlProvider.User.STORE)
    public String store(
            @ModelAttribute("userAdd") @Valid AdminAddUser data, 
            BindingResult br,
            @RequestParam("avatarFile") MultipartFile avatarFile,
            HttpSession session
    ) {
        if (!getRoleList().contains(data.getRole())) br.rejectValue("role", "error.role", "Invalid role value");
        if (br.hasErrors()) return "/admin/user/add";
        
        Users user = new Users();
        user.setEmail(data.getEmail());
        user.setEmailVerifyAt(new Date());
        user.setEmailVerifyCode(null);
        user.setForgotPasswordCode(null);
        user.setFullname(data.getFullname());
        user.setPassword(passwordEncoder.encode(data.getPassword()));
        user.setRole(data.getRole());
        
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                byte[] fileByte = avatarFile.getBytes();
                String originFileName = avatarFile.getOriginalFilename();
                String newFileName = FileSupport.saveFile(session.getServletContext().getRealPath("/"), "user", fileByte, originFileName);
                user.setAvatar(newFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            user.setAvatar(AppConstants.DEFAULT_AVATAR.toString());
        }
        
        usersFacade.create(user);
        
        return "redirect:/admin/user";
    }
    
    @GetMapping("" + AdminUrlProvider.User.EDIT)
    public ModelAndView edit(@PathVariable("id") int id, Model model) {
        Users user = usersFacade.find(id);
        
        if (user == null) return new ModelAndView("redirect:/admin/user");
        
        model.addAttribute("roles", getRoleList());
        
        AdminEditUser userEdit = AdminEditUser.builder()
                .userID(id)
                .avatar(user.getAvatar())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
        
        return new ModelAndView("/admin/user/edit", "userEdit", userEdit);
    }
    
    @PutMapping("" + AdminUrlProvider.User.UPDATE)
    public String update(
            @ModelAttribute("userEdit") @Valid AdminEditUser data, 
            BindingResult br,
            @RequestParam("avatarFile") MultipartFile avatarFile,
            HttpSession session
    ) {
        if (br.hasErrors()) return "/admin/user/edit";
        
        Users user = usersFacade.find(data.getUserID());
        
        if (user == null) return "redirect:/admin/user";
        
        user.setEmail(data.getEmail());
        user.setFullname(data.getFullname());
        if (data.getPassword() != null && !data.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(data.getPassword()));
        }
        user.setRole(data.getRole());
        
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                // Store new file
                byte[] fileByte = avatarFile.getBytes();
                String originFileName = avatarFile.getOriginalFilename();
                String realPath = session.getServletContext().getRealPath("/");
                String newFileName = FileSupport.saveFile(realPath, "user", fileByte, originFileName);
                
                // Delete old file
                if (!user.getAvatar().equals(AppConstants.DEFAULT_AVATAR.toString())) {
                    FileSupport.deleteFile(realPath, "user", user.getAvatar());
                }
                
                user.setAvatar(newFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        usersFacade.edit(user);
        
        return "redirect:/admin/user";
    }
    
    @GetMapping("" + AdminUrlProvider.User.DELETE)
    public ModelAndView showDeleteConfirm(@PathVariable("id") int id) {
        return new ModelAndView("/admin/user/delete", "userDelete", AdminDeleteRecord.builder().id(id).build());
    }
    
    @DeleteMapping("" + AdminUrlProvider.User.DELETE_CONFIRMATION)
    public String delete(
            @ModelAttribute("userDelete") @Valid AdminDeleteRecord data, 
            BindingResult br, 
            HttpSession session
    ) {
        if (br.hasErrors()) return "/admin/user/delete";
        
        Users user = usersFacade.find(data.getId());
        
        if (user == null) return "redirect:/admin/user";
        
        usersFacade.remove(user);
        
        try {
            FileSupport.deleteFile(session.getServletContext().getRealPath("/"), "user", user.getAvatar());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return "redirect:/admin/user";
    }
    
    private List<String> getRoleList() {
        List<String> roles = new ArrayList<>();
        
        roles.add(UserRoles.USER.toString());
        roles.add(UserRoles.ADMIN.toString());
        
        return roles;
    }

    private UsersFacadeLocal lookupUsersFacadeLocal() {
        try {
            Context c = new InitialContext();
            return (UsersFacadeLocal) c.lookup("java:global/EnterpriseAppAssignment/EnterpriseAppAssignment-ejb/UsersFacade!com.nosify.session_beans.UsersFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
