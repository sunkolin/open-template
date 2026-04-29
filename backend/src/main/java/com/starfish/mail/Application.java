package com.starfish.mail;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Application
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2018-05-23
 */
@Slf4j
@Configuration
@EnableScheduling
@EnableCaching
@SpringBootApplication
@MapperScan("com.starfish.mail.mapper")
public class Application {

    public static void main(String[] args) throws UnknownHostException {
        var app = SpringApplication.run(Application.class, args);
        Environment env = app.getEnvironment();
        
        // 获取配置信息
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        
        // 打印访问地址
        log.info("\n----------------------------------------------------------\n\t" +
                "Application is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:{}{}\n\t" +
                "External: \thttp://{}:{}{}\n\t" +
                "Login Page: \thttp://localhost:{}{}/login.html\n" +
                "----------------------------------------------------------",
                port, contextPath,
                hostAddress, port, contextPath,
                port, contextPath);
    }

}