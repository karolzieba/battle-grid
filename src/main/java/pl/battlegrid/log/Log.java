package pl.battlegrid.log;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pl.battlegrid.game.Game;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "LOG")
@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "COMMAND", nullable = false)
    private String command;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "ID_GAME", nullable = false)
    private Game game;

    public Log(String command, Game game) {
        this.command = command;
        this.game = game;
    }
}
