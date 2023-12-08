package com.nosify.models.user;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminAddUser {
    
    @NotNull
    @NotEmpty
    private String email;
    
    @NotNull
    @NotEmpty
    private String fullname;
    
    @NotNull
    @NotEmpty
    private String password;
    
    @NotNull
    @NotEmpty
    private String role;
}
