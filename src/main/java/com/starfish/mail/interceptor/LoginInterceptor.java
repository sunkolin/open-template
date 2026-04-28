package com.starfish.mail.interceptor;

import cn.hutool.core.util.StrUtil;
import com.starfish.mail.context.User;
import com.starfish.mail.context.UserContext;
import com.starfish.mail.entity.UserEntity;
import com.starfish.mail.service.UserService;
import com.starfish.mail.util.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器（基于JWT Token）
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-22
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求路径
        String uri = request.getRequestURI();
        
        // 放行静态资源和登录接口
        if (uri.startsWith("/static/") || 
            uri.endsWith(".html") || 
            uri.endsWith(".css") || 
            uri.endsWith(".js") ||
            uri.endsWith(".png") ||
            uri.endsWith(".jpg") ||
            uri.endsWith(".gif") ||
            uri.endsWith(".ico") ||
            uri.startsWith("/auth/login")) {
            return true;
        }
        
        // 从请求头获取Token
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token) || !token.startsWith("Bearer ")) {
            // 未登录，返回401状态码
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或登录已过期\"}");
            return false;
        }
        
        token = token.substring(7);
        
        // 验证Token
        if (!JwtUtil.verify(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Token无效或已过期\"}");
            return false;
        }
        
        // 从Token中获取用户ID
        Long userId = JwtUtil.getUserId(token);
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Token解析失败\"}");
            return false;
        }
        
        // 从数据库获取用户信息
        UserEntity userEntity = userService.selectById(userId);
        if (userEntity == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"用户不存在\"}");
            return false;
        }
        
        // 创建User对象并放入UserContext
        User user = new User();
        user.setUserId(userEntity.getId());
        user.setUserName(userEntity.getUserName());
        user.setNickName(userEntity.getNickName());
        UserContext.setUser(user);
        
        // 已登录，放行
        return true;
    }
}
