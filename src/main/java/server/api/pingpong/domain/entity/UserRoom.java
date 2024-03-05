package server.api.pingpong.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import server.api.pingpong.domain.state.Team;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoom{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_room_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Setter
    @Enumerated(EnumType.STRING)
    private Team team;
}
