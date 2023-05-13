package com.yww.file.config;

import com.yww.file.Interceptor.FileInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 *
 * </P>
 *
 * @author yww
 * @since 2023/5/14
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 上传文件不能为空的拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FileInterceptor()).addPathPatterns("/**");
    }

    /**
     * 重写跨域过滤器，实现全局跨域
     */
    @Bean
    public CorsFilter corsFilter() {
        // 1. 添加 CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //  放行全部原始域
        config.addAllowedOriginPattern("*");
        //  允许跨越发送cookie
        config.setAllowCredentials(true);
        //  放行哪些请求方式
        config.addAllowedMethod("*");
        //  放行全部原始头信息
        config.addAllowedHeader("*");
        //  放行所有头信息
        config.addExposedHeader("*");
        // 2. 添加映射路径
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", config);
        // 3. 返回新的CorsFilter
        return new CorsFilter(corsConfigurationSource);
    }

}
