package com.nosify.controllers.api;

import com.nosify.models.responses.DataResponse;
import com.nosify.models.test.UploadRequest;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestApiController {
    @GetMapping(value = "/demo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> demo() {
        DataResponse<String> res = new DataResponse<>();
        res.setSuccess(true);
        res.setMessage("Thanh cong");
        res.setData("OKE ROI NE");
        return new ResponseEntity(res, HttpStatus.OK);
    }
    
    @PostMapping(value = "/upload-something", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadSomething(@Valid @RequestBody UploadRequest reqBody, BindingResult br) throws MethodArgumentNotValidException {
        if (br.hasErrors()) {
            throw new MethodArgumentNotValidException(null, br);
        }
        if (reqBody.getName().equals("Nhu")) {
            br.rejectValue("name", "error.name", "Khong duoc ten Nhu");
        }
        if (br.hasErrors()) {
            throw new MethodArgumentNotValidException(null, br);
        }
        return ResponseEntity.ok(reqBody);
    }
}
