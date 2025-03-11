package pl.battlegrid.unit.enums;

import pl.battlegrid.unit.Unit;
import pl.battlegrid.unit.UnitDTO;
import pl.battlegrid.unit.type.AbstractUnitService;
import pl.battlegrid.unit.type.interfaces.Attackable;
import pl.battlegrid.unit.type.interfaces.Moveable;

import java.util.List;

public enum UnitCommandEnum {

    MOVEMENT {
        @Override
        public List<Unit> performCommand(AbstractUnitService abstractUnitService, List<Unit> units,
                                         UnitDTO dto, Integer gridSize) throws RuntimeException {
            if (abstractUnitService instanceof Moveable moveable) {
                return moveable.move(units, dto, gridSize);
            } else {
                throw new ClassCastException();
            }
        }
    },
    ATTACK {
        @Override
        public List<Unit> performCommand(AbstractUnitService abstractUnitService, List<Unit> units,
                                         UnitDTO dto, Integer gridSize) throws RuntimeException {
            if (abstractUnitService instanceof Attackable attackable) {
                return attackable.attack(units, dto, gridSize);
            } else {
                throw new ClassCastException();
            }
        }
    };

    public abstract List<Unit> performCommand(AbstractUnitService abstractUnitService, List<Unit> units,
                                              UnitDTO dto, Integer gridSize) throws RuntimeException;
}
