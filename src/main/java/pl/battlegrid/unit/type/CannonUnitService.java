package pl.battlegrid.unit.type;

import org.springframework.stereotype.Service;
import pl.battlegrid.unit.Unit;
import pl.battlegrid.unit.UnitDTO;
import pl.battlegrid.unit.enums.UnitTypeEnum;
import pl.battlegrid.unit.type.interfaces.Attackable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CannonUnitService extends AbstractUnitService implements Attackable {

    private static final Integer MAX_CELL_ATTACK_X = 1;

    private static final Integer MAX_CELL_ATTACK_Y = 2;

    private static final Integer ATTACK_COOLDOWN_IN_SECONDS = 13;

    @Override
    public UnitTypeEnum getType() {
        return UnitTypeEnum.CANNON;
    }

    @Override
    public List<Unit> attack(List<Unit> units, UnitDTO dto, Integer gridSize) throws RuntimeException {
        Unit selectedUnit = getUnit(units, dto);
        isCooldownActive(selectedUnit.getLastCommandTime(), ATTACK_COOLDOWN_IN_SECONDS);
        isActionPossible(selectedUnit.getPositionX(), selectedUnit.getPositionY(), dto.getPositionX(), dto.getPositionY(),
                MAX_CELL_ATTACK_X, MAX_CELL_ATTACK_Y, gridSize);
        units.stream()
                .filter(unit -> unit.getPositionX().equals(dto.getPositionX())
                        && unit.getPositionY().equals(dto.getPositionY()))
                .findFirst()
                .ifPresent(unitWithCoordinates -> {
                    if (!unitWithCoordinates.getColor().equals(selectedUnit.getColor())) {
                        unitWithCoordinates.setActive(false);
                    }
                });
        selectedUnit.setLastCommandTime(LocalDateTime.now());
        units.replaceAll(unit -> unit.getId().equals(selectedUnit.getId()) ? selectedUnit : unit);
        return units;
    }
}
