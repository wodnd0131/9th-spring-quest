package server.api.pingpong.domain.state;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Team {
    RED("RED"),
    BLUE("BLUE");

    private final String name;
}

