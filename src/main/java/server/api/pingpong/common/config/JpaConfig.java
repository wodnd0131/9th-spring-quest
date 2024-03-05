package server.api.pingpong.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import server.api.pingpong.common.utill.AuditorAwareImpl;

import java.time.LocalDateTime;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {
    @Bean
    public AuditorAware<LocalDateTime> auditorProvider() {
        return new AuditorAwareImpl();
    }
}

