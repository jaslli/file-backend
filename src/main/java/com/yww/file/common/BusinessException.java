package com.yww.file.common;

import lombok.Getter;

/**
 * <p>
 *      自定义异常类
 * </p>
 *
 * @author yww
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -1574716826948451793L;

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误信息
     */
    private final String message;

    public BusinessException(String message) {
        this.code = 500;
        this.message = message;
    }

}