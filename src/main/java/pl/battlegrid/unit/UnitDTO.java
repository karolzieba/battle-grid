package pl.battlegrid.unit;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UnitDTO {

    private Long id;
    private UnitTypeEnum type;
    private Integer positionX;
    private Integer positionY;
    private Boolean active;
    private LocalDateTime lastCommandTime;
}
