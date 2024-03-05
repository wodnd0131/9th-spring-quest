package server.api.pingpong.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.api.pingpong.common.utill.ApiResponse;
import server.api.pingpong.common.utill.ResponseType;
import server.api.pingpong.domain.dto.request.InitRequest;
import server.api.pingpong.domain.service.RoomService;
import server.api.pingpong.domain.service.UserRoomService;
import server.api.pingpong.domain.service.UserService;

@RestController
@RequiredArgsConstructor
@Tag(name = "init 컨트롤러", description = "초기 환경 세팅을 위한 API입니다.")
public class InitController {
    private final RoomService roomService;
    private final UserService userService;
    private final UserRoomService userRoomService;

    @Operation(summary = "헬스 체크 API", description = "서버와의 연결 상태를 확인합니다.")
    @GetMapping("/health")
    public ApiResponse<Object> checkHealthStatus() {
        return ApiResponse.of(ResponseType.REQUEST_SUCCESS);
    }
    @Operation(summary = "초기화 API", description = "유저 정보를 초기화합니다.")
    @PostMapping("/init")
    public ApiResponse<Object> initializeSystem(@RequestBody InitRequest request) {
        if (request.getQuantity() <= 0)  return ApiResponse.of(ResponseType.BAD_REQUEST);
        userRoomService.deleteAll();
        roomService.deleteAll();
        userService.deleteAll();
        return ApiResponse.of(userService.saveMemberInfoFromApiCall(request));
    }
}
