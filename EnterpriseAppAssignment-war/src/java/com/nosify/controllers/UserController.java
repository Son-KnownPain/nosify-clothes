package com.nosify.controllers;

import com.nosify.entities.Users;
import com.nosify.session_beans.UsersFacadeLocal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    UsersFacadeLocal usersFacade = lookupUsersFacadeLocal();
    
    @GetMapping("/sign-in")
    public String loginPage() {
        return "/user/login";
    }
    
    @GetMapping("/sign-up")
    public String registerPage() {
        return "/user/register";
    }
    
    @GetMapping("/verify/{userID}/{verifyCode}")
    public String verifyEmail(
            @PathVariable("userID") int userId,
            @PathVariable("verifyCode") String verifyCode
    )
    {
        Users user = usersFacade.find(userId);
        if (user == null) {
            return "/errors/404";
        } else {
            String userVerifyCode = user.getEmailVerifyCode();
            if (verifyCode.equals(userVerifyCode)) {
                user.setEmailVerifyCode(null);
                user.setEmailVerifyAt(new Date());
                usersFacade.edit(user);
            } else {
                return "/errors/404";
            }
        }
        
        return "/user/verify";
    }
    
    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "/user/forgot-password";
    }
    
    @GetMapping("/checkout")
    public String checkout() {
        return "/client/order";
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
