package pl.battlegrid.log;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pl.battlegrid.game.Game;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "LOG")
@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "COMMAND")
    private String command;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "ID_GAME")
    private Game game;
}
