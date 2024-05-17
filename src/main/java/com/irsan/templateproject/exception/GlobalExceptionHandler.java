package com.irsan.templateproject.exception;

import com.irsan.templateproject.utility.helper.ResponseHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.irsan.templateproject.utility.constant.GlobalConstant.ERROR;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 17/05/2024
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> noHandlerFoundException(NoHandlerFoundException e) {
        return ResponseHelper.build(e.getMessage(), ERROR, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidApiKeyException.class)
    public ResponseEntity<?> invalidApiKeyException(InvalidApiKeyException e) {
        return ResponseHelper.build(e.getMessage(), ERROR, HttpStatus.UNAUTHORIZED);
    }

}
