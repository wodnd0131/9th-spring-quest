package server.api.pingpong.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.api.pingpong.domain.entity.User;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FakerApiResponse {
    private String status;
    private int code;
    private int total;
    private List<Faker> data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Faker {
        private int id;
        private String uuid;
        private String firstname;
        private String lastname;
        private String username;
        private String password;
        private String email;
        private String ip;
        private String macAddress;
        private String website;
        private String image;
    }
}
