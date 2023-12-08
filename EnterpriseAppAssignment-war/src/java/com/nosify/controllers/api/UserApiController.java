package com.nosify.controllers.api;

import com.nosify.entities.Users;
import com.nosify.enums.AppConstants;
import com.nosify.enums.RequestAttributeKeys;
import com.nosify.enums.TokenNames;
import com.nosify.enums.UserRoles;
import com.nosify.exceptions.EmailNotVerifyException;
import com.nosify.models.responses.GeneralResponse;
import com.nosify.models.user.ChangeUserInfoRequest;
import com.nosify.models.user.ForgotPasswordRequest;
import com.nosify.models.user.GetUserInfoResponse;
import com.nosify.models.user.UserData;
import com.nosify.models.user.UserSignInRequest;
import com.nosify.models.user.UserSignInResponse;
import com.nosify.models.user.UserSignUpRequest;
import com.nosify.models.user.UserSignUpResponse;
import com.nosify.providers.UrlProvider;
import com.nosify.services.MailSenderService;
import com.nosify.services.UserTokenService;
import com.nosify.session_beans.UsersFacadeLocal;
import com.nosify.supports.ApplicationSupport;
import com.nosify.supports.FileSupport;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(UrlProvider.API_PREFIX + UrlProvider.User.PREFIX)
public class UserApiController {

    UsersFacadeLocal usersFacade = lookupUsersFacadeLocal();

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping(value = "" + UrlProvider.User.SIGN_UP, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAccount(@Valid @RequestBody UserSignUpRequest data, BindingResult br) throws MethodArgumentNotValidException {
        // Validation request body data
        if (usersFacade.findByEmail(data.getEmail()) != null) {
            br.rejectValue("email", "error.email", "Existing this email! Try other email");
        }
        if (!data.getPassword().equals(data.getPasswordConfirm())) {
            br.rejectValue("passwordConfirm", "error.passwordConfirm", "Password confirm not match");
        }
        if (br.hasErrors()) {
            throw new MethodArgumentNotValidException(null, br);
        }

        // Generate email verify code
        String emailVerifyCode = UUID.randomUUID().toString();

        // Create new user instance
        Users user = new Users();
        user.setEmail(data.getEmail().trim());
        user.setPassword(passwordEncoder.encode(data.getPassword()));
        user.setFullname("User");
        user.setAvatar(AppConstants.DEFAULT_AVATAR.toString());
        user.setEmailVerifyCode(emailVerifyCode);
        user.setEmailVerifyAt(null);
        user.setForgotPasswordCode(null);
        user.setRole(UserRoles.USER.toString());

        // Insert new user instance into DB
        try {
            usersFacade.create(user);
        } catch (ConstraintViolationException e) {
            for (Object constraintViolation : e.getConstraintViolations()) {
                System.out.println("CONSTRAINT: " + constraintViolation);
            }
        }

        // Send verify email
        // URL: /user/verify/{userID}/{verifyCode}
        String verifyEmailUrl = "/user/verify/" + user.getUserID() + "/" + emailVerifyCode;
        mailSenderService.sendVerifyLinkEmailMail(data.getEmail().trim(), verifyEmailUrl);

        // Respond to client
        return new ResponseEntity(new UserSignUpResponse(true, "Successfully sign up new account"), HttpStatus.OK);
    }

    @PostMapping(value = "" + UrlProvider.User.SIGN_IN, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @SuppressWarnings("null")
    public ResponseEntity<Object> signIn(@Valid @RequestBody UserSignInRequest data, BindingResult br, HttpServletResponse response) throws MethodArgumentNotValidException {
        // Get user
        Users user = usersFacade.findByEmail(data.getEmail());
        // Reject value if something were wrong
        if (user == null) {
            br.rejectValue("email", "error.email", "Email not found. Please sign up before!");
            throw new MethodArgumentNotValidException(null, br);
        }
        if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
            br.rejectValue("password", "error.password", "Incorrect password");
        }
        // Throw if has any error
        if (br.hasErrors()) {
            throw new MethodArgumentNotValidException(null, br);
        }
        if (user.getEmailVerifyAt() == null) {
            String emailVerifyCode = UUID.randomUUID().toString();
            user.setEmailVerifyCode(emailVerifyCode);
            usersFacade.edit(user);
            // Send verify email
            // URL: /user/verify/{userID}/{verifyCode}
            String verifyEmailUrl = "/user/verify/" + user.getUserID() + "/" + emailVerifyCode;
            mailSenderService.sendVerifyLinkEmailMail(data.getEmail().trim(), verifyEmailUrl);
            throw new EmailNotVerifyException("Your account is not verified!");
        }

        String accessToken = userTokenService.createAccessToken(user.getUserID() + "");
        String refreshToken = userTokenService.createRefreshToken(user.getUserID() + "");

        // Add AT and RT to cookie and set httpOnly to true
        Cookie accessTokenCookie = new Cookie(TokenNames.ACCESS_TOKEN.toString(), accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(60 * 60 * 24 * 365 * 60);
        accessTokenCookie.setPath("/");

        Cookie refreshTokenCookie = new Cookie(TokenNames.REFRESH_TOKEN.toString(), refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 365 * 60);
        refreshTokenCookie.setPath("/");

        // Add cookies to response
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        UserSignInResponse res = new UserSignInResponse();
        res.setSuccess(true);
        res.setMessage("Successfully sign in");
        res.setUser(
        UserData.builder()
            .userID(user.getUserID())
            .email(user.getEmail())
            .fullname(user.getFullname())
            .avatar(user.getAvatar())
            .build()
        );

        return new ResponseEntity(res, HttpStatus.OK);
    }

    @GetMapping(value = "" + UrlProvider.User.GET_USER_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserInfo(HttpServletRequest request) {
        String userID = (String) request.getAttribute(RequestAttributeKeys.USER_ID.toString());

        try {
            int userIdInt = Integer.parseInt(userID);

            Users user = usersFacade.find(userIdInt);
            
            UserData userData = UserData.builder()
                    .userID(user.getUserID())
                    .email(user.getEmail())
                    .fullname(user.getFullname())
                    .avatar(user.getAvatar())
                    .build();

            return new ResponseEntity(new GetUserInfoResponse(true, "Successfully get user info", userData), HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity(new GetUserInfoResponse(false, "User ID null", null), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(value = "" + UrlProvider.User.CHANGE_USER_INFO, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> changeUserInfo(
            @RequestPart(value = "avatar", required = false) MultipartFile avatar,
            @RequestPart(value = "data", required = true) @Valid ChangeUserInfoRequest data,
            BindingResult br,
            HttpServletRequest request,
            HttpSession session
    ) throws MethodArgumentNotValidException {
        if (br.hasErrors()) {
            throw new MethodArgumentNotValidException(null, br);
        }
        String userID = (String) request.getAttribute(RequestAttributeKeys.USER_ID.toString());
        try {
            int userIDInt = Integer.parseInt(userID);
            Users user = usersFacade.find(userIDInt);
            user.setFullname(data.getFullname());

            if (avatar != null && !avatar.isEmpty()) {
                // Handle avatar here
                byte[] avatarBytes = avatar.getBytes();
                String originalFileName = avatar.getOriginalFilename();
                String newAvatarFileName = FileSupport.saveFile(session.getServletContext().getRealPath("/"), "user", avatarBytes, originalFileName);
                user.setAvatar(newAvatarFileName);

            }
            usersFacade.edit(user);
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(UserApiController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(new GeneralResponse(false, "Something went wrong"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(new GeneralResponse(true, "Successfully change user info"), HttpStatus.OK);
    }
    
    @GetMapping(value = "" + UrlProvider.User.SIGN_OUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signOut(HttpServletRequest request, HttpServletResponse response) {
        Cookie aTCookie = new Cookie(TokenNames.ACCESS_TOKEN.toString(), "");
        Cookie rTCookie = new Cookie(TokenNames.REFRESH_TOKEN.toString(), "");
        
        aTCookie.setMaxAge(0);
        aTCookie.setPath("/");
        
        rTCookie.setMaxAge(0);
        rTCookie.setPath("/");
        
        response.addCookie(aTCookie);
        response.addCookie(rTCookie);
        
        return ResponseEntity.ok(new GeneralResponse(true, "Successfully sign out"));
    }
    
    // FORGOT PASSWORD
    @PostMapping(value = "" + UrlProvider.User.FORGOT_PASSWORD, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @SuppressWarnings("null")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordRequest data, BindingResult br) throws MethodArgumentNotValidException {
        Users user = usersFacade.findByEmail(data.getEmail());
        
        if (user == null) {
            br.rejectValue("email", "error.email", "Email not found!");
        }
        
        String forgotCode = data.getForgotPasswordCode();
        String newPassword = data.getNewPassword();
        String newPasswordConfirm = data.getNewPasswordConfirm();
        boolean verifyCode = false;
        
        if (forgotCode != null && !forgotCode.equals(user.getForgotPasswordCode())) {
            br.rejectValue("forgotPasswordCode", "error.forgotPasswordCode", "Code is not correct!");
        } else if (forgotCode != null && forgotCode.equals(user.getForgotPasswordCode())) {
            verifyCode = true;
        }
        
        if (verifyCode && newPassword != null && newPasswordConfirm != null) {
            if (!newPassword.equals(newPasswordConfirm)) {
                br.rejectValue("newPasswordConfirm", "error.newPasswordConfirm", "Password confirm not match!");
            } else {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setForgotPasswordCode(null);
                usersFacade.edit(user);
            }
        }
        
        if (br.hasErrors()) throw new MethodArgumentNotValidException(null, br);
        
        if (forgotCode == null) {
            String code = ApplicationSupport.generateForgotPasswordCode();
        
            user.setForgotPasswordCode(code);

            usersFacade.edit(user);

            mailSenderService.sendMimeEmailForgotPasswordCode(user.getEmail(), code, user.getFullname());
        }
        
        return ResponseEntity.ok(new GeneralResponse(true, "Successfully!"));
    }

    // USER FACADE
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
