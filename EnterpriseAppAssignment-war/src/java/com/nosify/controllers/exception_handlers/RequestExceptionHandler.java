package com.nosify.controllers.exception_handlers;

import com.nosify.exceptions.EmailNotVerifyException;
import com.nosify.exceptions.RecordNotFoundException;
import com.nosify.models.responses.EmailNotVerifyResponse;
import com.nosify.models.responses.GeneralResponse;
import com.nosify.models.responses.InvalidResponse;
import com.nosify.models.responses.MultipartExceptionResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice(annotations = RestController.class)
public class RequestExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            errors.add(error.getDefaultMessage());
        }
        InvalidResponse res = new InvalidResponse(false, "Invalid request data", true, errors);
        return new ResponseEntity<Object>(res, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(EmailNotVerifyException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseEntity<EmailNotVerifyResponse> handleEmailNotVerifyException(EmailNotVerifyException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(
                    new EmailNotVerifyResponse(false, true, ex.getLocalizedMessage())
                );
    }
    
    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<Object> handleMultipartException(MultipartException ex) {
        ex.printStackTrace();
        return new ResponseEntity(
                new MultipartExceptionResponse(false, ex.getMessage()), 
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    
    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<GeneralResponse> handleRecordNotFoundException(RecordNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                    GeneralResponse.builder()
                        .success(false)
                        .message(ex.getLocalizedMessage())
                        .build()
                );
    }
    
}
