package server.api.pingpong.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.pingpong.common.exception.BizException;
import server.api.pingpong.common.utill.ResponseType;
import server.api.pingpong.domain.dto.*;
import server.api.pingpong.domain.dto.request.CreateRoomRequest;
import server.api.pingpong.domain.dto.response.GetAllRoomsResponse;
import server.api.pingpong.domain.dto.response.RoomDetailResponse;
import server.api.pingpong.domain.entity.Room;
import server.api.pingpong.domain.entity.User;
import server.api.pingpong.domain.repository.RoomRepository;
import server.api.pingpong.domain.state.RoomType;
import server.api.pingpong.domain.state.Status;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public Room createRoom(CreateRoomRequest request, User user) {
        RoomType room_type = Arrays.stream(RoomType.values())
                .filter(rt -> rt.getName().equals(request.getRoomType()))
                .findFirst()
                .orElseThrow(() -> new BizException(ResponseType.BAD_REQUEST));
        Room room = Room.builder()
                .title(request.getTitle())
                .hostId(user)
                .roomType(room_type)
                .status(Status.WAIT).build();
        roomRepository.save(room);
        return room;
    }

    public GetAllRoomsResponse getAllRoomPage(int size, int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Room> roomPage = roomRepository.findAll(pageable);
        List<RoomDto> responseList = roomPage.getContent().stream()
                .map(room -> new RoomDto(room))
                .collect(Collectors.toList());

        return GetAllRoomsResponse.builder()
                .totalElements((int)roomPage.getTotalElements())
                .totalPages(roomPage.getTotalPages())
                .roomList(responseList)
                .build();
    }

    public RoomDetailResponse getRoomDetail(int roomId) {
        return new RoomDetailResponse(roomRepository.findById(roomId)
                .orElseThrow(() -> new BizException(ResponseType.BAD_REQUEST)));
    }

    public Room checkState(int roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BizException(ResponseType.BAD_REQUEST));
        if(!room.getStatus().equals(Status.WAIT)){
            throw new BizException(ResponseType.BAD_REQUEST);
        }
        return room;
    }

    public void changeRoomStatus(Status status, Room room) {
        room.setStatus(status);
        roomRepository.save(room);
    }
    public void deleteAll() {
        roomRepository.deleteAll();
    }
}
