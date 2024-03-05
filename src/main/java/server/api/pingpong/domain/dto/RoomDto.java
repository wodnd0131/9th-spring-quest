package server.api.pingpong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.api.pingpong.domain.entity.Room;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    protected int id;
    protected String title;
    protected int hostId;
    protected String roomType;
    protected String status;

    public RoomDto(Room room) {
        this.id=room.getId();
        this.hostId=room.getHostId().getId();
        this.roomType=room.getRoomType().getName();
        this.title=room.getTitle();
        this.status=room.getStatus().getName()  ;
    }
}