package com.starfish.mail.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 数据库初始化配置
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-22
 */
@Slf4j
@Configuration
public class DatabaseInitConfig {

    @Value("${spring.datasource.url:}")
    private String datasourceUrl;

    /**
     * 应用启动时创建数据库目录
     */
    @PostConstruct
    public void initDatabaseDirectory() {
        try {
            if (datasourceUrl == null || datasourceUrl.trim().isEmpty()) {
                log.warn("未配置数据源URL，跳过数据库目录初始化");
                return;
            }
            
            // 从JDBC URL中提取数据库文件路径
            // 格式: jdbc:sqlite:/path/to/database.db
            String dbPath = datasourceUrl.replace("jdbc:sqlite:", "");
            File dbFile = new File(dbPath);
            File parentDir = dbFile.getParentFile();
            
            if (parentDir != null && !parentDir.exists()) {
                log.info("创建数据库目录: {}", parentDir.getAbsolutePath());
                parentDir.mkdirs();
            }
            
            log.info("数据库文件路径: {}", dbFile.getAbsolutePath());
        } catch (Exception e) {
            log.error("初始化数据库目录失败", e);
        }
    }

    /**
     * 配置数据库初始化器
     */
    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        
        // 从database目录加载初始化脚本
        try {
            // 使用文件系统路径加载database/init.sql
            String projectRoot = System.getProperty("user.dir");
            File sqlFile = new File(projectRoot, "database/init.sql");
            if (sqlFile.exists()) {
                populator.addScript(new org.springframework.core.io.FileSystemResource(sqlFile));
                log.info("找到数据库初始化脚本: database/init.sql");
            } else {
                log.warn("未找到数据库初始化脚本: {}", sqlFile.getAbsolutePath());
            }
        } catch (Exception e) {
            log.error("加载数据库初始化脚本失败", e);
        }
        
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(populator);
        // 设置为true，每次启动都执行初始化脚本（因为使用了IF NOT EXISTS，所以是安全的）
        initializer.setEnabled(true);
        
        return initializer;
    }
}
