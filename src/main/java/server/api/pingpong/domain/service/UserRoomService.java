package server.api.pingpong.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.pingpong.common.exception.BizException;
import server.api.pingpong.common.utill.ResponseType;
import server.api.pingpong.domain.entity.Room;
import server.api.pingpong.domain.entity.User;
import server.api.pingpong.domain.entity.UserRoom;
import server.api.pingpong.domain.repository.UserRoomRepository;
import server.api.pingpong.domain.state.RoomType;
import server.api.pingpong.domain.state.Team;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRoomService {
    private final UserRoomRepository userRoomRepository;

    public boolean checkUserInRoom(int userId) {
        return userRoomRepository.findByUserId(userId).isEmpty();
    }


    public void createRelation(Room room, User user) {
        userRoomRepository.save(UserRoom.builder()
                .room(room)
                .user(user)
                .team(Team.RED)
                .build());
    }

    public void addRelation(Room room, User user) {
        Team team = Team.RED;
        long count_room = userRoomRepository.countByRoom(room);
        long count_red = userRoomRepository.countByRoomAndTeam(room, Team.RED);
        if (count_room/2 < count_red) {
            team = Team.BLUE;
        }
        userRoomRepository.save(UserRoom.builder()
                .room(room)
                .user(user)
                .team(team)
                .build());
    }

    public void checkUserCountInRoom(Room room) {
        long counting = userRoomRepository.countByRoom(room);
        boolean check_SINGLE = (room.getRoomType().equals(RoomType.SINGLE) && counting == 1);
        boolean check_DOUBLE = (room.getRoomType().equals(RoomType.DOUBLE) && counting < 4);
        if (!(check_SINGLE || check_DOUBLE)) {
            throw new BizException(ResponseType.BAD_REQUEST);
        }
    }

    public void checkUserCountIsEven(Room room) {
        long counting = userRoomRepository.countByRoom(room);
        if (counting % 2 == 0) {
            throw new BizException(ResponseType.BAD_REQUEST);
        }
    }

    public boolean DeleteRelation(Room room, User user) {
        if (room.getHostId().getId() == user.getId()) {
            userRoomRepository.deleteAllByRoom(room);
            return true;
        }
        userRoomRepository.deleteAllByUser(user);
        return false;
    }

    public void checkIsFull(Room room) {
        long counting = userRoomRepository.countByRoom(room);
        boolean check_SINGLE = (room.getRoomType().equals(RoomType.SINGLE) && counting == 2);
        boolean check_DOUBLE = (room.getRoomType().equals(RoomType.DOUBLE) && counting == 4);
        if (!(check_SINGLE || check_DOUBLE)) {
            throw new BizException(ResponseType.BAD_REQUEST);
        }
    }

    public void changeTeam(User user) {
        UserRoom data = userRoomRepository.findByUser(user);
        data.setTeam(data.getTeam() == Team.RED ? Team.BLUE : Team.RED);
    }

    public void deleteAll() {
        userRoomRepository.deleteAll();
    }
}
