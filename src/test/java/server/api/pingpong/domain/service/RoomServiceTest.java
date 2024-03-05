package server.api.pingpong.domain.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import server.api.pingpong.common.exception.BizException;
import server.api.pingpong.domain.dto.RoomDto;
import server.api.pingpong.domain.dto.request.CreateRoomRequest;
import server.api.pingpong.domain.dto.response.GetAllRoomsResponse;
import server.api.pingpong.domain.dto.response.RoomDetailResponse;
import server.api.pingpong.domain.entity.Room;
import server.api.pingpong.domain.entity.User;
import server.api.pingpong.domain.repository.RoomRepository;
import server.api.pingpong.domain.state.RoomType;
import server.api.pingpong.domain.state.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {
    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    void createRoom() {
        // Given
        CreateRoomRequest request = CreateRoomRequest.builder()
                .roomType("SINGLE(단식)").title("테스트 타이틀").build();

        User user = User.builder()
                .status(Status.ACTIVE)
                .build();

        Room expectedRoom = Room.builder()
                .title(request.getTitle())
                .hostId(user)
                .roomType(RoomType.SINGLE)
                .status(Status.WAIT)
                .build();

        when(roomRepository.save(any(Room.class))).thenReturn(expectedRoom);

        // When
        Room resultRoom = roomService.createRoom(request, user);
        // Then
        assertEquals(expectedRoom.getTitle(), resultRoom.getTitle());
        assertEquals(expectedRoom.getHostId(), resultRoom.getHostId());
        assertEquals(expectedRoom.getRoomType(), resultRoom.getRoomType());
        assertEquals(expectedRoom.getStatus(), resultRoom.getStatus());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void getAllRoomPage() {
        // Given
        int size = 10;
        int page = 1;

        List<Room> rooms = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            User user = User.builder()
                    .status(Status.ACTIVE)
                    .build();

            Room room = Room.builder()
                    .title("Room " + i)
                    .hostId(user)
                    .roomType(RoomType.SINGLE)
                    .status(Status.WAIT)
                    .build();
            rooms.add(room);
        }

        Page<Room> roomPage = new PageImpl<>(rooms);
        when(roomRepository.findAll(any(Pageable.class))).thenReturn(roomPage);

        // When
        GetAllRoomsResponse response = roomService.getAllRoomPage(size, page);

        // Then
        assertEquals(roomPage.getTotalElements(), response.getTotalElements());
        assertEquals(roomPage.getTotalPages(), response.getTotalPages());
        assertEquals(roomPage.getContent().size(), response.getRoomList().size());
        for (int i = 0; i < roomPage.getContent().size(); i++) {
            Room room = roomPage.getContent().get(i);
            RoomDto roomDto = response.getRoomList().get(i);
            assertEquals(room.getTitle(), roomDto.getTitle());
        }
    }

    @Test
    void getRoomDetail() {
        // Given
        int roomId = 1;
        User user = User.builder()
                .status(Status.ACTIVE)
                .build();
        Room room = Room.builder()
                .title("Room 1")
                .hostId(user)
                .roomType(RoomType.SINGLE)
                .status(Status.WAIT)
                .build();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        // When
        RoomDetailResponse response = roomService.getRoomDetail(roomId);

        // Then
        assertEquals(room.getTitle(), response.getTitle());
    }

    @Test
    void getRoomDetail_notFound() {
        // Given
        int roomId = 1;
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // Then
        assertThrows(BizException.class, () -> {
            // When
            roomService.getRoomDetail(roomId);
        });
    }

    @Test
    void checkState() {
        // Given
        int roomId = 1;
        Room room = Room.builder()
                .status(Status.WAIT)
                .build();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        // When
        Room resultRoom = roomService.checkState(roomId);

        // Then
        assertEquals(room.getStatus(), resultRoom.getStatus());
    }

    @Test
    void checkState_notFound() {
        // Given
        int roomId = 1;
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // Then
        assertThrows(BizException.class, () -> {
            // When
            roomService.checkState(roomId);
        });
    }

    @Test
    void changeRoomStatus() {
        // Given
        Room room = Room.builder()
                .status(Status.WAIT)
                .build();
        Status status = Status.ACTIVE;

        // When
        roomService.changeRoomStatus(status, room);

        // Then
        assertEquals(status, room.getStatus());
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void deleteAll() {
        // When
        roomService.deleteAll();

        // Then
        verify(roomRepository, times(1)).deleteAll();
    }
}