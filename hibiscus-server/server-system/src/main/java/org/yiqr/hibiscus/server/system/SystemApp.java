package org.yiqr.hibiscus.server.system;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author rose
 * @create 2023/8/5 23:32
 */
@Slf4j
@RefreshScope
@EnableCaching
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class SystemApp {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(SystemApp.class);
        Environment env = app.run(args).getEnvironment();
        log.info(
                """
                        
                ----------------------------------------------------------
                        Application profiles active: '{}'
                        Application '{}' is running! Access URLs:
                        Local:http://127.0.0.1:{}
                        External: http://{}:{}
                ----------------------------------------------------------""",
                env.getProperty("spring.profiles.active"),
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
    }
}
