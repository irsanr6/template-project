package com.irsan.templateproject.utility.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 17/05/2024
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHelper<T> implements Serializable {

    private static final long serialVersionUID = 31346947918272439L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalRow;

    private Long timestamp;

    private Integer status;

    public static <T> ResponseEntity<ResponseHelper<?>> build(T data, String message, Integer total, HttpStatus status) {
        return ResponseEntity.status(status.value()).body(
                ResponseHelper.builder()
                        .data(data)
                        .message(message)
                        .totalRow(total)
                        .timestamp(System.currentTimeMillis())
                        .status(status.value())
                        .build()
        );
    }

    public static <T> ResponseEntity<ResponseHelper<?>> build(T data, Integer total, HttpStatus status) {
        return ResponseEntity.status(status.value()).body(
                ResponseHelper.builder()
                        .data(data)
                        .totalRow(total)
                        .timestamp(System.currentTimeMillis())
                        .status(status.value())
                        .build()
        );
    }

    public static <T> ResponseEntity<ResponseHelper<Object>> build(T data, String message, HttpStatus status) {
        return ResponseEntity.status(status.value()).body(
                ResponseHelper.builder()
                        .data(data)
                        .message(message)
                        .timestamp(System.currentTimeMillis())
                        .status(status.value())
                        .build()
        );
    }

    public static <T> ResponseEntity<ResponseHelper<Object>> build(T data, HttpStatus status) {
        return ResponseEntity.status(status.value()).body(
                ResponseHelper.builder()
                        .data(data)
                        .timestamp(System.currentTimeMillis())
                        .status(status.value())
                        .build()
        );
    }

    public static <T> ResponseEntity<ResponseHelper<Object>> build(String message, HttpStatus status) {
        return ResponseEntity.status(status.value()).body(
                ResponseHelper.builder()
                        .message(message)
                        .timestamp(System.currentTimeMillis())
                        .status(status.value())
                        .build()
        );
    }

}
