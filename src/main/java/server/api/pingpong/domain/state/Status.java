package server.api.pingpong.domain.state;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    WAIT("WAIT(대기)"),
    ACTIVE("ACTIVE(활성)"),
    NON_ACTIVE("NON_ACTIVE(비활성)"),

    PROGRESS("PROGRESS(진행중)"),
    FINISH("FINISH(완료)");

    private final String name;

//    @JsonValue
//    public String getName() {
//        return name;
//    }
}

