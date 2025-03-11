package pl.battlegrid.unit.type.interfaces;

import pl.battlegrid.unit.Unit;
import pl.battlegrid.unit.UnitDTO;

import java.util.List;

public interface Moveable {

    List<Unit> move(List<Unit> units, UnitDTO dto, Integer gridSize) throws RuntimeException;
}
