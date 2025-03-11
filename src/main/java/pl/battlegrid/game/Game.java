package pl.battlegrid.game;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pl.battlegrid.log.Log;
import pl.battlegrid.unit.Unit;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "GAME")
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "GRID_SIZE", nullable = false)
    private Integer gridSize;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "game", cascade = { CascadeType.PERSIST })
    private List<Unit> units;

    @OneToMany(mappedBy = "game")
    private List<Log> logs;

    public Game(Integer gridSize, List<Unit> units) {
        this.gridSize = gridSize;
        units.forEach(unit -> unit.setGame(this));
        this.units = units;
    }
}
