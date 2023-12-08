package com.nosify.models.user;

import javax.validation.constraints.Size;
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
public class UserSignInRequest {
    @NotEmpty(message = "Email can not empty")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotEmpty(message = "Password can not empty")
    @Size(min = 8, max = 100, message = "Invalid size, from 8 to 100")
    private String password;
}
