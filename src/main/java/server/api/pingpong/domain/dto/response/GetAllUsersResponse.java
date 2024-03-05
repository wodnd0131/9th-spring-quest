package server.api.pingpong.domain.dto.response;

import lombok.Builder;
import lombok.Getter;
import server.api.pingpong.domain.dto.UserDto;
import server.api.pingpong.domain.entity.User;

import java.util.List;

@Getter
@Builder
public class GetAllUsersResponse {
    private int totalElements;
    private int totalPages;
    private List<UserDto> userList;
}
