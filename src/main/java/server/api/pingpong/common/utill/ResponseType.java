package server.api.pingpong.common.utill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.pingpong.common.utill.BaseResponseType;

@Getter
@AllArgsConstructor
public enum ResponseType implements BaseResponseType {
    REQUEST_SUCCESS(200, "API 요청이 성공했습니다.", HttpStatus.OK),
    BAD_REQUEST(201, "불가능한 요청입니다.", HttpStatus.BAD_REQUEST),
    SERVER_ERROR(500, "에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
