package app.xlog.ggbond;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.redisson.api.listener.StreamAddListener;
import org.redisson.api.stream.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@Slf4j
@SpringBootTest
public class AppTest {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 测试日志采集
     */
    @Test
    void name() {
        log.info("测试测试测试");
    }

    /**
     * 测试 redisson 的 stream
     */
    @Test
    void testStream() {
        RStream<String, String> logStream = redissonClient.getStream(MonitorCenterConstant.RedisKey.BIG_MARKET_LOG_STREAM);
        logStream.createGroup(StreamCreateGroupArgs.name("group1").makeStream());
        logStream.createConsumer("group1", "consumer1");

        StreamMessageId streamMessageId = logStream.add(StreamAddArgs.entries(Map.of(
                "level", "INFO",
                "message", "测试测试测试"
        )));
        // Map<StreamMessageId, Map<String, String>> read = logStream.read(StreamReadArgs.greaterThan(new StreamMessageId(1742466827955L)));
        // Map<StreamMessageId, Map<String, String>> read = logStream.read(StreamReadArgs.greaterThan(new StreamMessageId(0L)));
        // System.out.println(read);

        // 组消费
        // Map<StreamMessageId, Map<String, String>> streamMessageIdMapMap = logStream.readGroup(
        //         "group1",
        //         "consumer1",
        //         StreamReadGroupArgs.neverDelivered());
        Map<StreamMessageId, Map<String, String>> streamMessageIdMapMap = logStream.readGroup(
                "group1",
                "consumer1",
                StreamReadGroupArgs.greaterThan(new StreamMessageId(0L)));
        System.out.println(streamMessageIdMapMap);
    }

    /**
     * 测试 redisson 的 stream 监听器
     */
    @Test
    @SneakyThrows
    void testStreamListener() {
        RStream<String, String> logStream = redissonClient.getStream(MonitorCenterConstant.RedisKey.BIG_MARKET_LOG_STREAM);
        logStream.addListener((StreamAddListener) name -> {
            System.out.println(name);
            System.out.println("监听到消息：" + name);
        });
        StreamMessageId streamMessageId = logStream.add(StreamAddArgs.entries(Map.of(
                "level", "INFO",
                "message", "测试测试测试"
        )));
        Thread.sleep(5000L);
    }

}