package server.api.pingpong.common.utill;

import org.springframework.data.domain.AuditorAware;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<LocalDateTime> {

    @Override
    public Optional<LocalDateTime> getCurrentAuditor() {
        return Optional.of(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
    }
}
