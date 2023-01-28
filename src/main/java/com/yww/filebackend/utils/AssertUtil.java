package com.yww.filebackend.utils;

import com.yww.filebackend.common.GlobalException;

/**
 * <p>
 *      断言工具类
 * </p>
 *
 * @author yww
 */
public class AssertUtil {


    /**
     * 私有化无参构造器
     */
    private AssertUtil() {}

    /**
     * 如果对象为{@code null}, 则抛出异常
     *
     * @param object  要判断的对象
     * @param message 断言失败时的错误信息
     * @throws GlobalException 全局异常类
     */
    public static void notNull(Object object, String message) throws GlobalException {
        if (object == null) {
            throw new GlobalException(message);
        }
    }

}
