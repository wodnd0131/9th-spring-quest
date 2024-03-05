package server.api.pingpong.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.api.pingpong.domain.entity.Room;
import server.api.pingpong.domain.entity.User;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {}
