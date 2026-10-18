package com.shop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShopApiApplication {

    private static final Logger log = LoggerFactory.getLogger(ShopApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ShopApiApplication.class, args);
    }

    @Bean
    @Order(100)
    public CommandLineRunner startupAnnouncement() {
        return args -> {
            log.info("\n============================================================================");
            log.info("🚀 SHOP API SERVER ĐÃ KHỞI ĐỘNG XONG VÀ ĐANG CHẠY TRÊN CỔNG 8080!");
            log.info("👉 Swagger UI (Tài liệu API): http://localhost:8080/api/v1/swagger-ui/index.html");
            log.info("👉 UI Prototype (Giao diện Web): http://localhost:4010");
            log.info("ℹ️  Server đang lắng nghe các request. Giữ nguyên cửa sổ này (KHÔNG bấm Ctrl+C).");
            log.info("============================================================================\n");
        };
    }
}
