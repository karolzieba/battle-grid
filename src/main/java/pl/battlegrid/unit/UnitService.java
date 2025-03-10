package pl.battlegrid.unit;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class UnitService {

    private final UnitRepository repository;

    private static final Integer GRID_FIRST_ROW = 0;
    private static final Integer GRID_PLAYER_ROWS = 2;

    @Value("${game.settings.archers}")
    private Integer archers;

    @Value("${game.settings.transports}")
    private Integer transports;

    @Value("${game.settings.cannons}")
    private Integer cannons;

    public List<UnitDTO> getGameUnits(Long idGame, UnitColorEnum color) {
        List<Unit> units = repository.findAllByGameIdAndColor(idGame, color);
        if (units == null || units.isEmpty()) return new ArrayList<>();
        return units.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<Unit> createNewUnits(Integer gridSize) throws IllegalArgumentException {
        Integer numberOfUnits = archers + transports + cannons;
        isNumberOfUnitsCorrect(gridSize, numberOfUnits);
        Random random = new Random();
        return new ArrayList<>() {{
            addAll(createNewUnitsForPlayer(random, UnitColorEnum.BLACK, gridSize, GRID_FIRST_ROW));
            addAll(createNewUnitsForPlayer(random, UnitColorEnum.WHITE, gridSize, gridSize - GRID_PLAYER_ROWS));
        }};
    }

    private UnitDTO mapToDTO(Unit unit) {
        return new UnitDTO() {{
            setId(unit.getId());
            setType(unit.getType());
            setPositionX(unit.getPositionX());
            setPositionY(unit.getPositionY());
            setActive(unit.getActive());
            setLastCommandTime(unit.getLastCommandTime());
        }};
    }

    private void isNumberOfUnitsCorrect(Integer gridSize, Integer numberOfUnits) throws IllegalArgumentException {
        if (numberOfUnits > (gridSize * GRID_PLAYER_ROWS)) throw new IllegalArgumentException();
    }

    private List<Unit> createNewUnitsForPlayer(Random random, UnitColorEnum color, int gridSize, int startRowNumber) {
        List<UnitTypeEnum> types = new ArrayList<>();
        for (int i = 0; i < archers; i++) {
            types.add(UnitTypeEnum.ARCHER);
        }
        for (int i = 0; i < transports; i++) {
            types.add(UnitTypeEnum.TRANSPORT);
        }
        for (int i = 0; i < cannons; i++) {
            types.add(UnitTypeEnum.CANNON);
        }
        return IntStream.range(startRowNumber, startRowNumber + GRID_PLAYER_ROWS)
                .boxed()
                .flatMap(positionY -> IntStream.range(0, gridSize)
                        .mapToObj(positionX -> {
                            if (!types.isEmpty()) {
                                int idType = random.nextInt(types.size());
                                UnitTypeEnum type = types.get(idType);
                                types.remove(idType);
                                return new Unit(type, color, positionX, positionY);
                            }
                            return null;
                        }))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
