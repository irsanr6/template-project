package com.irsan.templateproject.exception;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 17/05/2024
 */
public class InvalidApiKeyException extends RuntimeException {

    public InvalidApiKeyException(String message) {
        super(message);
    }

    public InvalidApiKeyException(Throwable cause) {
        super(cause);
    }

}
