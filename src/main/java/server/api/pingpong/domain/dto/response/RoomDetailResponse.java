package server.api.pingpong.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import server.api.pingpong.domain.dto.RoomDto;
import server.api.pingpong.domain.entity.Room;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailResponse extends RoomDto {
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public RoomDetailResponse(Room room) {
        super(room);
        this.createdAt=room.getCreatedAt();
        this.updatedAt=room.getUpdatedAt();
    }
}