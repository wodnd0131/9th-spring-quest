package server.api.pingpong.common.exception;

import lombok.Getter;
import server.api.pingpong.common.utill.BaseResponseType;

@Getter
public class BizException extends RuntimeException{
    private final BaseResponseType baseExceptionType;

    public BizException(BaseResponseType baseExceptionType){
        super(baseExceptionType.getMessage());
        this.baseExceptionType = baseExceptionType;
    }

}
