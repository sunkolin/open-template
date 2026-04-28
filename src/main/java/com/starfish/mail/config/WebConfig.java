package com.starfish.mail.config;

import com.starfish.mail.interceptor.LoginInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * WebConfig
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2019-07-04
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    /**
     * 添加静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源访问路径
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    /**
     * 添加视图控制器（默认跳转到登录页）
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 根路径跳转到登录页
        registry.addViewController("/").setViewName("forward:/login.html");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册登录拦截器
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login.html",
                        "/index.html",
                        "/users.html",
                        "/settings.html",
                        "/auth/login",
                        "/static/**"
                );
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "DELETE", "PUT")
//                .maxAge(3600 * 24);
//    }

}
