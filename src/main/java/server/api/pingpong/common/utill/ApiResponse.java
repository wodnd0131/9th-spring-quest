package server.api.pingpong.common.utill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T result;

    public ApiResponse(final Integer status, final String message){
        this.code = status;
        this.message = message;
        this.result = null;
    }

    public static<T> ApiResponse<T> of(final BaseResponseType response){
        return of(response, null);
    }

    public static<T> ApiResponse<T> of(final BaseResponseType response, final T t){
        return ApiResponse.<T>builder()
                .code(response.getCode())
                .message(response.getMessage())
                .result(t)
                .build();
    }
}