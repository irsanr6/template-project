package com.irsan.templateproject.exception;

import com.irsan.templateproject.utility.helper.ResponseHelper;
import com.irsan.templateproject.utility.util.FileUtil;
import com.irsan.templateproject.utility.util.LoggerUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.irsan.templateproject.utility.constant.GlobalConstant.ERROR;
import static com.irsan.templateproject.utility.constant.GlobalConstant.ERROR_STACKTRACE_LOG_FILE;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 17/05/2024
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LoggerUtil log = new LoggerUtil(GlobalExceptionHandler.class);
    private final FileUtil fileUtil;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception e) {
        printStackTrace(e);
        return ResponseHelper.build("Please check your logs for more error information", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> appException(AppException e) {
        printStackTrace(e);
        return ResponseHelper.build(e.getMessage(), ERROR, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> noHandlerFoundException(NoHandlerFoundException e) {
        printStackTrace(e);
        return ResponseHelper.build(e.getMessage(), ERROR, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidApiKeyException.class)
    public ResponseEntity<?> invalidApiKeyException(InvalidApiKeyException e) {
        printStackTrace(e);
        return ResponseHelper.build(e.getMessage(), ERROR, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> unauthorizedException(UnauthorizedException e) {
        printStackTrace(e);
        return ResponseHelper.build(e.getMessage(), ERROR, HttpStatus.UNAUTHORIZED);
    }

    private void printStackTrace(Throwable e) {
        String requestId = MDC.get("requestId");
        fileUtil.printStackTrace(e, String.format(ERROR_STACKTRACE_LOG_FILE, requestId));
        log.error(e.getMessage());
    }

}
