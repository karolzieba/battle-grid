package pl.battlegrid.unit.type;

import org.springframework.stereotype.Service;
import pl.battlegrid.unit.Unit;
import pl.battlegrid.unit.UnitDTO;
import pl.battlegrid.unit.enums.UnitTypeEnum;
import pl.battlegrid.unit.type.interfaces.Moveable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransportUnitService extends AbstractUnitService implements Moveable {

    private static final Integer MAX_CELL_MOVEMENT = 3;

    private static final Integer MOVEMENT_COOLDOWN_IN_SECONDS = 7;

    @Override
    public UnitTypeEnum getType() {
        return UnitTypeEnum.TRANSPORT;
    }

    @Override
    public List<Unit> move(List<Unit> units, UnitDTO dto, Integer gridSize) throws RuntimeException {
        Unit selectedUnit = getUnit(units, dto);
        isCooldownActive(selectedUnit.getLastCommandTime(), MOVEMENT_COOLDOWN_IN_SECONDS);
        isActionPossible(selectedUnit.getPositionX(), selectedUnit.getPositionY(), dto.getPositionX(), dto.getPositionY(),
                MAX_CELL_MOVEMENT, MAX_CELL_MOVEMENT, gridSize);
        Unit unitWithCoordinates = units.stream()
                .filter(unit -> unit.getPositionX().equals(dto.getPositionX())
                        && unit.getPositionY().equals(dto.getPositionY()))
                .findFirst()
                .orElse(null);
        if (unitWithCoordinates != null) {
            if (!unitWithCoordinates.getColor().equals(selectedUnit.getColor())) {
                unitWithCoordinates.setActive(false);
                selectedUnit.setPositionX(dto.getPositionX());
                selectedUnit.setPositionY(dto.getPositionY());
            }
        } else {
            selectedUnit.setPositionX(dto.getPositionX());
            selectedUnit.setPositionY(dto.getPositionY());
        }
        selectedUnit.setLastCommandTime(LocalDateTime.now());
        units.replaceAll(unit -> unit.getId().equals(selectedUnit.getId()) ? selectedUnit : unit);
        return units;
    }
}
