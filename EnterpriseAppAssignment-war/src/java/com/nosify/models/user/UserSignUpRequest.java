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
public class UserSignUpRequest {
    @NotEmpty(message = "Email can not empty")
    @Email(message = "Email invalid format")
    private String email;
    @NotEmpty(message = "Password can not empty")
    @Size(min = 8, max = 26, message = "Password length least at 8 characters and max 26 characters")
    private String password;
    @NotEmpty(message = "Password confirm can not empty")
    @Size(min = 8, max = 26, message = "Password confirm length least at 8 characters and max 26 characters")
    private String passwordConfirm;
}
