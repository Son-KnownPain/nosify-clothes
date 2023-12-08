package com.nosify.models.category;

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
public class AdminAddCategory {
    @NotNull
    @NotEmpty
    private String name;
    
    @NotNull
    @NotEmpty
    private String description;
    
    private String thumbnail;
}
