package pl.battlegrid.unit.type;

import jakarta.persistence.EntityNotFoundException;
import pl.battlegrid.unit.Unit;
import pl.battlegrid.unit.UnitDTO;
import pl.battlegrid.unit.enums.UnitTypeEnum;

import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractUnitService {

    public abstract UnitTypeEnum getType();

    protected Unit getUnit(List<Unit> units, UnitDTO dto) throws RuntimeException {
        return units.stream()
                .filter(unit -> unit.getId().equals(dto.getId()))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
    }

    protected void isCooldownActive(LocalDateTime lastCommandTime, Integer cooldown) throws RuntimeException {
        LocalDateTime currentTime = LocalDateTime.now();
        lastCommandTime.plusSeconds(cooldown);
        if (currentTime.isBefore(lastCommandTime)) throw new IllegalStateException();
    }

    protected void isActionPossible(Integer oldPositionX, Integer oldPositionY,
                                    Integer newPositionX, Integer newPositionY,
                                    Integer maxCellValueX, Integer maxCellValueY,
                                    Integer gridSize) throws RuntimeException {
        if (Math.abs(newPositionX - oldPositionX) > maxCellValueX
                || Math.abs(newPositionY - oldPositionY) > maxCellValueY
                || (newPositionX < 0 || newPositionX > (gridSize - 1))
                || (newPositionY < 0 || newPositionY > (gridSize - 1))) throw new IllegalArgumentException();
    }
}
