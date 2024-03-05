package server.api.pingpong.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import server.api.pingpong.domain.state.RoomType;
import server.api.pingpong.domain.state.Status;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int id;
    private String title;

    @ManyToOne
    @JoinColumn(name = "host")
    private User hostId;

    @Setter
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    @Setter
    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonIgnore
    @OneToMany(mappedBy = "room",  fetch = FetchType.LAZY)
    private List<UserRoom> userRooms;
}
