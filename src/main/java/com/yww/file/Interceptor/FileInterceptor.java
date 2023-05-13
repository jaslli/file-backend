package com.yww.file.Interceptor;

import com.yww.file.common.Result;
import com.yww.file.utils.ResponseUtil;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * <p>
 *      文件拦截器
 * </P>
 *
 * @author yww
 * @since 2023/5/14
 */
public class FileInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 判断请求是否为POST请求
        if (!request.getMethod().equals(HttpMethod.POST.name())) {
            return true;
        }

        // 判断请求是否上传文件
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // 获取参数
            for (Parameter parameter : method.getParameters()) {
                // 如果参数类型是文件的话，进行判断
                if (parameter.getType().equals(MultipartFile.class)) {
                    MultipartFile file = ((MultipartHttpServletRequest) request).getFile(parameter.getName());
                    if (file == null || file.isEmpty()) {
                        ResponseUtil.response(response, Result.failure("上传的文件不能为空"));
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
