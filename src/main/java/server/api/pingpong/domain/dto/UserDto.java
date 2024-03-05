package server.api.pingpong.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import server.api.pingpong.domain.entity.BaseTimeEntity;
import server.api.pingpong.domain.entity.Room;
import server.api.pingpong.domain.entity.User;
import server.api.pingpong.domain.entity.UserRoom;
import server.api.pingpong.domain.state.Status;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private int id;
    private int fakerId;
    private String name;
    private String email;
    private Status status;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static UserDto of(User user){
        UserDto userDto = new UserDto();
        userDto.id = user.getId();
        userDto.fakerId = user.getFakerId();
        userDto.name = user.getName();
        userDto.email = user.getEmail();
        userDto.status = user.getStatus();
        userDto.createdAt = user.getCreatedAt();
        userDto.updatedAt = user.getUpdatedAt();
        return userDto;
    }
}
