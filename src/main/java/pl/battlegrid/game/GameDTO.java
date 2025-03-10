package pl.battlegrid.game;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GameDTO {

    private Long id;
    private Integer gridSize;
    private Boolean active;
    private LocalDateTime createdAt;
}
