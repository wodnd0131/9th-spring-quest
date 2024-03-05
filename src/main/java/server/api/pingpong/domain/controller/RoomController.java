package server.api.pingpong.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.api.pingpong.common.exception.BizException;
import server.api.pingpong.common.utill.ApiResponse;
import server.api.pingpong.common.utill.ResponseType;
import server.api.pingpong.domain.dto.request.CreateRoomRequest;
import server.api.pingpong.domain.dto.request.UserIdRequest;
import server.api.pingpong.domain.dto.response.GetAllRoomsResponse;
import server.api.pingpong.domain.dto.response.RoomDetailResponse;
import server.api.pingpong.domain.entity.Room;
import server.api.pingpong.domain.entity.User;
import server.api.pingpong.domain.service.RoomService;
import server.api.pingpong.domain.service.UserRoomService;
import server.api.pingpong.domain.service.UserService;
import server.api.pingpong.domain.state.Status;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@Tag(name = "Room 컨트롤러", description = "방을 생성하거나 조회하는 API입니다.")
public class RoomController {
    private final RoomService roomService;
    private final UserService userService;
    private final UserRoomService userRoomService;
    @Operation(summary = "방 생성 API", description = "방을 생성합니다.")
    @PostMapping("/room")
    public ApiResponse<Object> createRoom(@RequestBody CreateRoomRequest request) {
        try {
            User user = userService.isActive(request.getUserId());
            if(!userRoomService.checkUserInRoom(request.getUserId()))
                throw new BizException(ResponseType.BAD_REQUEST);
            Room room = roomService.createRoom(request, user);
            userRoomService.createRelation(room, user);
        }catch (BizException e){
            e.printStackTrace();
            return ApiResponse.of(ResponseType.BAD_REQUEST);
        }
        return ApiResponse.of(ResponseType.REQUEST_SUCCESS);
    }
    @Operation(summary = "방 전체 조회 API", description = "방 전체를 페이지 단위로 조회합니다.")
    @GetMapping("/room")
    public ApiResponse<GetAllRoomsResponse> getAllUsers(@RequestParam int size, @RequestParam int page) {
        if (page < 0 || size <= 0)  return ApiResponse.of(ResponseType.BAD_REQUEST);;
        return ApiResponse.of(ResponseType.REQUEST_SUCCESS,roomService.getAllRoomPage(size,page));
    }
    @Operation(summary = "방 상세 조회 API", description = "방 상세 정보를 조회합니다.")
    @GetMapping("/room/{roomId}")
    public ApiResponse<RoomDetailResponse> getRoomDetail(@PathVariable int roomId) {
        RoomDetailResponse room;
        try {
        room = roomService.getRoomDetail(roomId);
        }catch (BizException e){
            e.printStackTrace();
            return ApiResponse.of(ResponseType.BAD_REQUEST);
        }
        return ApiResponse.of(ResponseType.REQUEST_SUCCESS,room);
    }
    @Operation(summary = "방 참가 API", description = "방에 참가합니다.")
    @PostMapping("/room/attention/{roomId}")
        public ApiResponse<Object> joinRoom(@PathVariable int roomId, @RequestBody UserIdRequest request) {
            try {
                User user = userService.isActive(request.getUserId());
                Room room = roomService.checkState(roomId);

                if(!userRoomService.checkUserInRoom(request.getUserId()))
                    throw new BizException(ResponseType.BAD_REQUEST);
                userRoomService.checkUserCountInRoom(room);
                userRoomService.addRelation(room,user);
            }catch (BizException e){
                e.printStackTrace();
                return ApiResponse.of(ResponseType.BAD_REQUEST);
        }
        return ApiResponse.of(ResponseType.REQUEST_SUCCESS);
    }
    @Operation(summary = "방 나가기 API", description = "방에서 나갑니다.")
    @PostMapping("/room/out/{roomId}")
    public ApiResponse<Object> leaveRoom(@PathVariable int roomId, @RequestBody UserIdRequest request) {
        try {
            if(userRoomService.checkUserInRoom(request.getUserId()))
                throw new BizException(ResponseType.BAD_REQUEST);

            User user = userService.findUser(request.getUserId());
            Room room = roomService.checkState(roomId);

            if(userRoomService.DeleteRelation(room,user))
                roomService.changeRoomStatus(Status.FINISH,room);
        }catch (BizException e){
            e.printStackTrace();
            return ApiResponse.of(ResponseType.BAD_REQUEST);
        }
        return ApiResponse.of(ResponseType.REQUEST_SUCCESS);
    }
    @Operation(summary = "게임 시작 API", description = "게임을 시작합니다.")
    @PutMapping("/room/start/{roomId}")
    public ApiResponse<Object> startGame(@PathVariable int roomId, @RequestBody UserIdRequest request) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        try {
            Room room = roomService.checkState(roomId);
            if(room.getHostId().getId()!=request.getUserId())
                throw new BizException(ResponseType.BAD_REQUEST);
            userRoomService.checkIsFull(room);
            roomService.changeRoomStatus(Status.PROGRESS, room);
            executorService.schedule(() -> {roomService.changeRoomStatus(Status.FINISH, room);}, 1, TimeUnit.MINUTES);
        } catch (BizException e) {
            e.printStackTrace();
            return ApiResponse.of(ResponseType.BAD_REQUEST);
        } finally {
            executorService.shutdown();
        }
        return ApiResponse.of(ResponseType.REQUEST_SUCCESS);
    }
    @Operation(summary = "팀 변경 API", description = "팀을 변경합니다.")
    @PutMapping("/team/{roomId}")
    public ApiResponse<Object> changeTeam(@PathVariable int roomId, @RequestBody UserIdRequest request) {
        try {
            if(userRoomService.checkUserInRoom(request.getUserId()))
                throw new BizException(ResponseType.BAD_REQUEST);
            Room room = roomService.checkState(roomId);
            User user = userService.findUser(request.getUserId());
            userRoomService.checkUserCountIsEven(room);
            userRoomService.changeTeam(user);
        }catch (BizException e){
            e.printStackTrace();
            return ApiResponse.of(ResponseType.BAD_REQUEST);
        }
        return ApiResponse.of(ResponseType.REQUEST_SUCCESS);
    }
}
