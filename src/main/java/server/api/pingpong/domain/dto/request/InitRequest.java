package server.api.pingpong.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InitRequest {
    private int seed;
    private int quantity;
}
