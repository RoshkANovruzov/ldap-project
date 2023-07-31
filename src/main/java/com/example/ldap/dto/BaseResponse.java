package com.example.ldap.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<R> {
    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime timestamp;
    private Integer status;
    private String key;
    private String message;
    private R data;
    private List<ValidationError> errors;

    public static <R> BaseResponse<R> success(R data, String key) {
        return new BaseResponseBuilder<R>()
                .status(HttpStatus.OK.value())
                .key(key)
                .data(data)
                .build();
    }

    public static <R> BaseResponse<R> success(R data) {
        return new BaseResponseBuilder<R>()
                .status(HttpStatus.OK.value())
                .key("SUCCESS")
                .data(data)
                .build();
    }

    public static BaseResponse<Void> success(String key) {
        return new BaseResponseBuilder<Void>()
                .status(HttpStatus.OK.value())
                .key(key)
                .build();
    }

    public static BaseResponse<Void> failure(int httpStatusCode, String key, String message) {
        return new BaseResponseBuilder<Void>()
                .timestamp(OffsetDateTime.now())
                .status(httpStatusCode)
                .key(key)
                .message(message)
                .build();
    }

    public static <R> BaseResponse<R> failure(int httpStatusCode, String key, R data) {
        return new BaseResponseBuilder<R>()
                .timestamp(OffsetDateTime.now())
                .status(httpStatusCode)
                .key(key)
                .data(data)
                .build();
    }

    public static BaseResponse<Void> failure(int httpStatusCode, String key) {
        return new BaseResponseBuilder<Void>()
                .timestamp(OffsetDateTime.now())
                .status(httpStatusCode)
                .key(key)
                .build();
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) errors = new ArrayList<>();
        errors.add(new ValidationError(field, message));
    }

    private record ValidationError(String field, String Message) {
    }

}