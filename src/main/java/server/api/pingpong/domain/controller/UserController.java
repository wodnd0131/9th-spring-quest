package server.api.pingpong.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.api.pingpong.common.utill.ApiResponse;
import server.api.pingpong.common.utill.ResponseType;
import server.api.pingpong.domain.dto.response.GetAllUsersResponse;
import server.api.pingpong.domain.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User 컨트롤러", description = "유저 정보를 조회하는 API입니다.")
public class UserController {
    private final UserService userService;

    @Operation(summary = "유저 전체 조회 API", description = "유저 전체를 페이지 단위로 조회합니다.")
    @GetMapping()
    public ApiResponse<GetAllUsersResponse> getAllUsers(@RequestParam int size, @RequestParam int page) {
        if (page < 0 || size <= 0)  return ApiResponse.of(ResponseType.BAD_REQUEST);;
        return ApiResponse.of(ResponseType.REQUEST_SUCCESS,userService.getUserPage(size,page));
    }
}
