package com.nosify.models.test;

import javax.validation.constraints.Min;
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
public class UploadRequest {
    @NotNull(message = "Khong thay id")
    public int id;
    
    @NotEmpty(message = "Khong duoc rong")
    public String name;
    
    @NotNull
    @Min(value = 18, message = "Lon hon 18")
    public int age;
}
