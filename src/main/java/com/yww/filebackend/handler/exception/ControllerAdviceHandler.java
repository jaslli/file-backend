package com.yww.filebackend.handler.exception;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yww.filebackend.common.GlobalException;
import com.yww.filebackend.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *      全局异常处理
 * </p>
 *
 * @author yww
 */
@RestControllerAdvice
@Slf4j
public class ControllerAdviceHandler {

    /**
     * 处理自定义的服务异常信息
     * 统一处理GlobalException异常，异常处理顺序是从小异常到大异常。
     *
     * @param e 服务异常
     * @return 异常信息
     */
    @ExceptionHandler(value = GlobalException.class)
    public <T> Result<T> globalExceptionHandler(GlobalException e, HttpServletRequest request) {
        log.error(">> global exception: {}, {}, {}", request.getRequestURI(), e.getCode(), e.getMessage());
        String errMessage = e.getMessage();
        // 防止空的错误信息
        if (StringUtils.isBlank(errMessage)) {
            errMessage = "服务器繁忙";
        }
        return Result.failure(e.getCode(), errMessage);
    }

    /**
     * 异常信息
     *
     * @param e 服务异常
     * @return 异常信息
     */
    @ExceptionHandler(value = Exception.class)
    public <T> Result<T> defaultErrorHandler(Exception e, HttpServletRequest request) {
        log.error(">> 服务器内部错误: {}, {} " , request.getRequestURI(), e.getMessage());
        return Result.failure(500, "服务器繁忙");
    }

}
