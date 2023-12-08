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
public class ChangeUserInfoRequest {
    @NotNull(message = "Fullname can not null")
    @NotEmpty(message = "Fullname can not empty")
    private String fullname;
}
