package server.api.pingpong.domain.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class CreateRoomRequest {
    private int userId;
    private String roomType;
    private String title;
}
