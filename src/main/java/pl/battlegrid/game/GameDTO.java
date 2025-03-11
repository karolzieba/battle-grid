package pl.battlegrid.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameDTO {

    private Long id;
    private Integer gridSize;
    private Boolean active;
    private LocalDateTime createdAt;
}
