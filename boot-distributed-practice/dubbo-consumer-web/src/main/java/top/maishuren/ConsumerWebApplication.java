package top.maishuren;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class ConsumerWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerWebApplication.class, args);
    }
}
