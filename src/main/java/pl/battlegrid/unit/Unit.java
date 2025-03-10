package pl.battlegrid.unit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import pl.battlegrid.game.Game;

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
    @Column(name = "TYPE")
    private UnitTypeEnum type;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "COLOR")
    private UnitColorEnum color;

    @Column(name = "POSITION_X")
    private Integer positionX;

    @Column(name = "POSITION_Y")
    private Integer positionY;

    @Column(name = "ACTIVE")
    private Boolean active;

    @UpdateTimestamp
    @Column(name = "LAST_COMMAND_TIME")
    private LocalDateTime lastCommandTime;

    @ManyToOne
    @JoinColumn(name = "ID_GAME")
    private Game game;

    public Unit(UnitTypeEnum type, UnitColorEnum color, Integer positionX, Integer positionY) {
        this.type = type;
        this.color = color;
        this.positionX = positionX;
        this.positionY = positionY;
        this.lastCommandTime = LocalDateTime.now();
    }
}
