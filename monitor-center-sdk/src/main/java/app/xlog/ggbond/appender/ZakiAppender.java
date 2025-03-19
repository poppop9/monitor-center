package app.xlog.ggbond.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * 自定义采集器
 */
public class ZakiAppender extends AppenderBase<ILoggingEvent> {
    @Override
    protected void append(ILoggingEvent loggingEvent) {
        String formattedMessage = loggingEvent.getFormattedMessage();
        System.out.println(formattedMessage);
    }
}