package com.yww.filebackend.common;

import com.yww.filebackend.common.constant.ResultCode;
import lombok.Data;

/**
 * <p>
 *      结果封装类
 * </p>
 *
 * @author yww
 */
@Data
public class Result<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回内容
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 私有化无参构造函数
     */
    private Result() {
    }

    /**
     * 全参构造函数
     *
     * @param code    状态码
     * @param message 返回内容
     * @param data    返回数据
     */
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回一个响应成功的信息，不带数据
     *
     * @return Result
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getStatus(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 返回一个响应成功的信息，带数据
     *
     * @param data 返回数据
     * @return Result
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getStatus(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 返回一个响应错误的信息
     *
     * @return Result
     */
    public static <T> Result<T> failure() {
        return new Result<>(ResultCode.FAILED.getStatus(), ResultCode.FAILED.getMessage(), null);
    }

    /**
     * 返回一个响应错误的信息，可以自定义错误信息
     *
     * @param message 返回的错误信息内容
     * @return Result
     */
    public static <T> Result<T> failure(String message) {
        return new Result<>(ResultCode.FAILED.getStatus(), message, null);
    }

    /**
     * 根据结果状态码，返回一个响应错误的信息
     *
     * @param resultCode 状态码
     * @return Result
     */
    public static <T> Result<T> failure(ResultCode resultCode) {
        return new Result<>(resultCode.getStatus(), resultCode.getMessage(), null);
    }

    /**
     * 返回一个自定义状态码和错误信息的错误提示
     *
     * @param code    状态码
     * @param message 消息
     * @return Result
     */
    public static <T> Result<T> failure(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 返回一个自定义状态码和错误信息的错误提示
     *
     * @param code    状态码
     * @param message 消息
     * @param date    错误信息
     * @return Result
     */
    public static <T> Result<T> failure(Integer code, String message, T date) {
        return new Result<>(code, message, date);
    }

}