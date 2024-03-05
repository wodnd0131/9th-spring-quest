package server.api.pingpong.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.api.pingpong.domain.entity.Room;
import server.api.pingpong.domain.entity.User;
import server.api.pingpong.domain.entity.UserRoom;
import server.api.pingpong.domain.state.Team;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoomRepository extends JpaRepository<UserRoom, Integer> {
    Optional<Object> findByUserId(int userId);
    long  countByRoom(Room room);
    long  countByRoomAndTeam(Room room, Team team);
    void deleteAllByRoom(Room room);
    void deleteAllByUser(User user);
    UserRoom findByUser(User user);
}
