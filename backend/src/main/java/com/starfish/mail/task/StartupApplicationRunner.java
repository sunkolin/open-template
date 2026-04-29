package com.starfish.mail.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;

/**
 * StartupApplicationRunner
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2019-01-06
 */
@Slf4j
@Component
public class StartupApplicationRunner implements ApplicationRunner {

    /**
     * 启动时执行
     *
     * @param args 参数
     */
    @Override
    public void run(ApplicationArguments args) throws MessagingException {
        log.info("StartupApplicationRunner run.");
    }

}
