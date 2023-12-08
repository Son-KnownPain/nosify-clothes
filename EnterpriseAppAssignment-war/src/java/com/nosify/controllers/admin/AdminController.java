package com.nosify.controllers.admin;

import com.nosify.entities.Users;
import com.nosify.enums.UserRoles;
import com.nosify.models.user.AdminUserLogin;
import com.nosify.providers.AdminUrlProvider;
import com.nosify.session_beans.UsersFacadeLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("" + AdminUrlProvider.ADMIN_PREFIX)
public class AdminController {

    UsersFacadeLocal usersFacade = lookupUsersFacadeLocal();

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @GetMapping({"/", ""})
    public String index() {
        return "/admin/index";
    }
    
    @GetMapping(AdminUrlProvider.Admin.LOGIN + "")
    public ModelAndView loginPage() {
        return new ModelAndView("admin/login", "accountLogin", new AdminUserLogin());
    }
    
    @PostMapping("" + AdminUrlProvider.Admin.LOGIN_CHECK)
    public String loginCheck(@ModelAttribute("accountLogin") @Valid AdminUserLogin data, BindingResult br, HttpServletRequest request) {
        if (br.hasErrors()) return "/admin/login";
        
        Users user = usersFacade.findByEmail(data.getEmail());
        
        if (user == null || !user.getRole().equals(UserRoles.ADMIN.toString())) {
            br.rejectValue("email", "error.email", "Email not found!");
            return "/admin/login";
        }
        
        if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
            br.rejectValue("password", "error.password", "Incorrect password!");
            return "/admin/login";
        }
        
        request.getSession().setAttribute("is_login", "logged_in");
        
        return "redirect:/admin";
    }
    
    @GetMapping("" + AdminUrlProvider.Admin.LOGOUT)
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("is_login");
        return "redirect:/admin";
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
