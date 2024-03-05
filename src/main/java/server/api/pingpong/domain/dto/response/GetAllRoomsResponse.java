package server.api.pingpong.domain.dto.response;

import lombok.*;
import server.api.pingpong.domain.dto.RoomDto;

import java.util.List;

@Getter
@Builder
public class GetAllRoomsResponse {
    private int totalElements;
    private int totalPages;
    private List<RoomDto> roomList;
}
