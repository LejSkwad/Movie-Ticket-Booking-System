package com.mtb.authservice.exception;


import com.mtb.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException e, HttpServletRequest request) {
        return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(
                e.getStatus(),
                e.getErrorCode(),
                e.getMessage(),
                request.getRequestURI()
        ));
    }

}
