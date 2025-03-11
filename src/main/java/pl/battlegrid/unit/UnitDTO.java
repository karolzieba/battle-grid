package pl.battlegrid.unit;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import pl.battlegrid.unit.enums.UnitColorEnum;
import pl.battlegrid.unit.enums.UnitCommandEnum;
import pl.battlegrid.unit.enums.UnitTypeEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnitDTO {

    private Long id;
    private UnitTypeEnum type;
    private UnitColorEnum color;
    private UnitCommandEnum command;
    private Integer positionX;
    private Integer positionY;
    private Boolean active;
    private LocalDateTime lastCommandTime;
}
