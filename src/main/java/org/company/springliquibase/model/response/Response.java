package org.company.springliquibase.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.company.springliquibase.enums.Result;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Response<T> {

    Integer code;
    String message;
    T data;
    Map<String, Integer> categoryProductCountMap;

    public Response(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Response<T> success(String message, T data, Map<String, Integer> categoryProductCountMap) {
        return new Response<>(Result.SUCCESS.getCode(), message, data, categoryProductCountMap);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(Result.SUCCESS.getCode(), Result.SUCCESS.getMessage(), data);
    }

    public static <T> Response<T> success(T data, String message) {
        return new Response<>(Result.SUCCESS.getCode(), message, data);
    }

    public static <T> Response<T> success(String message, T data) {
        return new Response<>(Result.SUCCESS.getCode(), message, data);
    }

    public static <T> Response<T> failed(Integer code, String message) {
        return new Response<>(code, message, null);
    }

    public static <T> Response<T> response(Integer code, String message, T data) {
        return new Response<>(code, message, data);
    }

    public static <T> Response<T> error(String message) {
        return new Response<>(Result.ERROR.getCode(), message, null);
    }
} 