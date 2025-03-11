package pl.battlegrid.unit.type;

import org.springframework.stereotype.Service;
import pl.battlegrid.unit.Unit;
import pl.battlegrid.unit.UnitDTO;
import pl.battlegrid.unit.enums.UnitTypeEnum;
import pl.battlegrid.unit.type.interfaces.Attackable;
import pl.battlegrid.unit.type.interfaces.Moveable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArcherUnitService extends AbstractUnitService implements Moveable, Attackable {

    private static final Integer MAX_CELL_MOVEMENT = 1;
    private static final Integer MAX_CELL_ATTACK = 2;
    private static final Integer MOVEMENT_COOLDOWN_IN_SECONDS = 5;
    private static final Integer ATTACK_COOLDOWN_IN_SECONDS = 10;

    @Override
    public UnitTypeEnum getType() {
        return UnitTypeEnum.ARCHER;
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

    @Override
    public List<Unit> attack(List<Unit> units, UnitDTO dto, Integer gridSize) throws RuntimeException {
        Unit selectedUnit = getUnit(units, dto);
        isCooldownActive(selectedUnit.getLastCommandTime(), ATTACK_COOLDOWN_IN_SECONDS);
        isActionPossible(selectedUnit.getPositionX(), selectedUnit.getPositionY(), dto.getPositionX(), dto.getPositionY(),
                MAX_CELL_ATTACK, MAX_CELL_ATTACK, gridSize);
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
