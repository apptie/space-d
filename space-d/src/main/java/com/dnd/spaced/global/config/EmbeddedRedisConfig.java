package com.dnd.spaced.global.config;

import com.dnd.spaced.global.config.exception.EmbeddedRedisExecuteException;
import com.dnd.spaced.global.config.exception.EmbeddedRedisPortException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import redis.embedded.RedisServer;

@Profile("local || test")
@Configuration
public class EmbeddedRedisConfig {

    private static final int START_PORT_NUMBER = 10000;
    private static final int END_PORT_NUMBER = 65535;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws IOException {
        int port = isRedisRunning() ? findAvailablePort() : redisPort;

        redisServer = initRedisServer(port);

        try {
            redisServer.start();
        } catch (Exception e) {
            throw new EmbeddedRedisExecuteException("embedded redis를 실행할 수 없습니다.", e);
        }
    }

    private RedisServer initRedisServer(int port) {
        if (isArmMac()) {
            return new RedisServer(getRedisFileForArcMac(), port);
        }

        return new RedisServer(port);
    }

    private boolean isArmMac() {
        return "aarch64".equals(System.getProperty("os.arch")) && "Mac OS X".equals(System.getProperty("os.name"));
    }

    private File getRedisFileForArcMac() {
        try {
            return new ClassPathResource("binary/redis/redis-server-7.2.3-mac-arm64").getFile();
        } catch (Exception e) {
            throw new EmbeddedRedisExecuteException("embedded redis를 실행하기 위한 redis binary를 찾을 수 없습니다.", e);
        }
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }

    public int findAvailablePort() throws IOException {
        for (int port = START_PORT_NUMBER; port <= END_PORT_NUMBER; port++) {
            Process process = executeGrepProcessCommand(port);

            if (!isRunning(process)) {
                return port;
            }
        }

        throw new EmbeddedRedisPortException("사용 가능한 포트가 없습니다.");
    }

    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisPort));
    }

    private Process executeGrepProcessCommand(int redisPort) throws IOException {
        String command = String.format("netstat -nat | grep LISTEN | grep %d", redisPort);
        String[] shell = {"/bin/sh", "-c", command};

        return Runtime.getRuntime()
                      .exec(shell);
    }

    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }
        } catch (Exception e) {
            throw new EmbeddedRedisExecuteException("embedded redis를 실행할 수 없습니다.", e);
        }

        return !pidInfo.isEmpty() && !pidInfo.toString().isBlank();
    }
}
