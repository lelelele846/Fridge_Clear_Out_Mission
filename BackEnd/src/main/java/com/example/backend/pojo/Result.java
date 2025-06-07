package com.example.backend.pojo;

import lombok.Data;
import org.apache.ibatis.annotations.Insert;

@Data
public class Result {
    private Integer code;//编码：1成功，0为失败
    private String message;
    private Object data;

    public static Result success() {
        Result result = new Result();
        result.code = 1;
        result.message = "success";
        result.data = null;
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.code = 1;
        result.message = "success";
        result.data = data;
        return result;
    }

    public static Result error() {
        Result result = new Result();
        result.code = 0;
        result.message = "error";
        result.data = null;
        return result;
    }

    public static Result error(String message) {
        Result result = new Result();
        result.code = 0;
        result.message = message == null ? "error" : message;
        result.data = null;
        result.message = message;
        return result;
    }
}
