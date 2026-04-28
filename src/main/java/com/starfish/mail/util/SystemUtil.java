package com.starfish.mail.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

/**
 * SystemUtil
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2019-03-31
 */
@Slf4j
public class SystemUtil {

    /**
     * 获取SystemLoadAverage
     *
     * @return 结果
     */
    public static double getSystemLoadAverage() {
        double systemLoadAverage = 0;
        try {
            OperatingSystemMXBean operatingSystem = ManagementFactory.getOperatingSystemMXBean();
            systemLoadAverage = operatingSystem.getSystemLoadAverage();
        } catch (Exception e) {
            log.error("SystemUtil getSystemLoadAverage() error.", e);
        }

        return systemLoadAverage;
    }

}
