package app.xlog.ggbond.config;

import app.xlog.ggbond.appender.ZakiAppender;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Logback 配置
 */
@Configuration
public class LogbackConfig {
    /**
     * 将自定义的 Appender 添加到 Logback 中
     */
    @PostConstruct
    public void init() {
        // 获取 Logback 上下文
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset(); // 清空现有配置

        // 1. 创建自定义 Appender
        ZakiAppender zakiAppender = new ZakiAppender();
        zakiAppender.setContext(context);
        zakiAppender.start();

        // 3. 将 Appender 绑定到 Root Logger
        Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(zakiAppender);
    }
}
