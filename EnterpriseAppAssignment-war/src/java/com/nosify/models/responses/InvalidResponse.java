package com.nosify.models.responses;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlRootElement(name = "error")
public class InvalidResponse {
    private boolean success;
    private String message;
    private boolean invalid;
    private List<String> errors;
}
