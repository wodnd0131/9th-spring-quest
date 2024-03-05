package server.api.pingpong.domain.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.api.pingpong.common.exception.BizException;
import server.api.pingpong.domain.entity.Room;
import server.api.pingpong.domain.entity.User;
import server.api.pingpong.domain.entity.UserRoom;
import server.api.pingpong.domain.repository.UserRoomRepository;
import server.api.pingpong.domain.state.RoomType;
import server.api.pingpong.domain.state.Status;
import server.api.pingpong.domain.state.Team;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRoomServiceTest {
    @Mock
    private UserRoomRepository userRoomRepository;

    @InjectMocks
    private UserRoomService userRoomService;

    @Test
    void checkUserInRoom() {
        // Given
        int userId = 1;
        when(userRoomRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // When
        boolean result = userRoomService.checkUserInRoom(userId);

        // Then
        assertTrue(result);
    }

    @Test
    void createRelation() {
        // Given
        Room room = Room.builder()
                .status(Status.WAIT).build();
        User user = User.builder()
                .status(Status.ACTIVE).build();
        when(userRoomRepository.save(any(UserRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        userRoomService.createRelation(room, user);

        // Then
        verify(userRoomRepository, times(1)).save(any(UserRoom.class));
    }

    @Test
    void addRelation() {
        // Given
        Room room = Room.builder()
                .status(Status.WAIT).build();
        User user = User.builder()
                .status(Status.ACTIVE).build();
        when(userRoomRepository.countByRoom(room)).thenReturn(2L);
        when(userRoomRepository.countByRoomAndTeam(room, Team.RED)).thenReturn(1L);
        when(userRoomRepository.save(any(UserRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        userRoomService.addRelation(room, user);

        // Then
        verify(userRoomRepository, times(1)).save(any(UserRoom.class));
    }

    @Test
    void checkUserCountInRoom() {
        // Given
        Room room = Room.builder()
                .status(Status.WAIT).roomType(RoomType.SINGLE).build();
        when(userRoomRepository.countByRoom(room)).thenReturn(1L);

        // When
        // Then
        assertDoesNotThrow(() -> userRoomService.checkUserCountInRoom(room));

        room.setRoomType(RoomType.DOUBLE);
        when(userRoomRepository.countByRoom(room)).thenReturn(2L);

        // When
        // Then
        assertDoesNotThrow(() -> userRoomService.checkUserCountInRoom(room));
    }

    @Test
    void checkUserCountIsEven() {
        // Given
        Room room = Room.builder()
                .status(Status.WAIT).build();
        when(userRoomRepository.countByRoom(room)).thenReturn(2L);

        // When
        // Then
        assertThrows(BizException.class, () -> userRoomService.checkUserCountIsEven(room));
    }

    @Test
    void DeleteRelation() {
        // Given
        User user = User.builder()
                .id(1).status(Status.ACTIVE).build();
        Room room = Room.builder()
                .hostId(user).status(Status.WAIT).build();
        lenient().doNothing().when(userRoomRepository).deleteAllByRoom(room);
        lenient().doNothing().when(userRoomRepository).deleteAllByUser(user);

        // When
        boolean result = userRoomService.DeleteRelation(room, user);

        // Then
        assertTrue(result);
        verify(userRoomRepository, times(1)).deleteAllByRoom(room);
    }

    @Test
    void checkIsFull() {
        // Given
        Room room = Room.builder()
                .status(Status.WAIT).roomType(RoomType.SINGLE).build();
        when(userRoomRepository.countByRoom(room)).thenReturn(2L);

        // When
        // Then
        assertDoesNotThrow(() -> userRoomService.checkIsFull(room));

        room.setRoomType(RoomType.DOUBLE);
        when(userRoomRepository.countByRoom(room)).thenReturn(4L);

        // When
        // Then
        assertDoesNotThrow(() -> userRoomService.checkIsFull(room));
    }

    @Test
    void changeTeam() {
        // Given
        User user = User.builder()
                .status(Status.ACTIVE).build();
        UserRoom userRoom = UserRoom.builder()
                .user(user).build();
        userRoom.setTeam(Team.RED);
        when(userRoomRepository.findByUser(user)).thenReturn(userRoom);

        // When
        userRoomService.changeTeam(user);

        // Then
        assertEquals(Team.BLUE, userRoom.getTeam());
    }

    @Test
    void deleteAll() {
        // Given
        doNothing().when(userRoomRepository).deleteAll();

        // When
        userRoomService.deleteAll();

        // Then
        verify(userRoomRepository, times(1)).deleteAll();
    }

}
