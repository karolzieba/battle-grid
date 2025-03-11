package pl.battlegrid.unit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.battlegrid.game.Game;
import pl.battlegrid.unit.enums.UnitColorEnum;
import pl.battlegrid.unit.enums.UnitTypeEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "UNIT")
@Entity
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private UnitTypeEnum type;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "COLOR", nullable = false)
    private UnitColorEnum color;

    @Column(name = "POSITION_X", nullable = false)
    private Integer positionX;

    @Column(name = "POSITION_Y", nullable = false)
    private Integer positionY;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = true;

    @Column(name = "LAST_COMMAND_TIME", nullable = false)
    private LocalDateTime lastCommandTime;

    @ManyToOne
    @JoinColumn(name = "ID_GAME", nullable = false)
    private Game game;

    public Unit(UnitTypeEnum type, UnitColorEnum color, Integer positionX, Integer positionY) {
        this.type = type;
        this.color = color;
        this.positionX = positionX;
        this.positionY = positionY;
        this.lastCommandTime = LocalDateTime.now();
    }
}
