package com.nosify.models.user;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgotPasswordRequest {
    @NotNull(message = "Email can not null")
    @NotEmpty(message = "Email can not empty")
    @Email(message = "Email is not correct format")
    private String email;
    
    private String forgotPasswordCode;
    
    private String newPassword;
    
    private String newPasswordConfirm;
}
