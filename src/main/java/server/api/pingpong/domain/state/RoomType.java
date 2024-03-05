package server.api.pingpong.domain.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoomType {
    SINGLE("SINGLE(단식)"),
    DOUBLE("DOUBLE(복식)");

    private final String name;

//    @JsonValue // 출력시 name 값으로 출력, 시나리오 결과물과 다르니 제외
//    public String getName() {
//        return name;
//    }
}

