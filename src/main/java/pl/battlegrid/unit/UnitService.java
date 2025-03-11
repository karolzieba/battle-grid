package pl.battlegrid.unit;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.battlegrid.game.Game;
import pl.battlegrid.log.LogService;
import pl.battlegrid.unit.enums.UnitColorEnum;
import pl.battlegrid.unit.enums.UnitCommandEnum;
import pl.battlegrid.unit.enums.UnitTypeEnum;
import pl.battlegrid.unit.type.AbstractUnitService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UnitService {

    private final UnitRepository repository;
    private final LogService logService;
    private final Map<UnitTypeEnum, AbstractUnitService> serviceStrategies;

    private static final Integer GRID_FIRST_ROW = 0;
    private static final Integer GRID_PLAYER_ROWS = 2;

    @Value("${game.settings.archers}")
    private Integer archers;

    @Value("${game.settings.transports}")
    private Integer transports;

    @Value("${game.settings.cannons}")
    private Integer cannons;

    @Autowired
    public UnitService(UnitRepository unitRepository,
                       LogService logService,
                       Map<String, AbstractUnitService> unitServices) {
        this.repository = unitRepository;
        this.logService = logService;
        this.serviceStrategies = unitServices.values().stream()
                .collect(Collectors.toMap(AbstractUnitService::getType, Function.identity()));
    }

    public List<UnitDTO> getGameUnits(Long idGame, UnitColorEnum color) {
        List<Unit> units = repository.findAllByGameIdAndColor(idGame, color);
        if (units == null || units.isEmpty()) return new ArrayList<>();
        return units.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void performCommand(UnitDTO dto) throws RuntimeException {
        Unit unit = repository.findByIdAndColor(dto.getId(), dto.getColor())
                .orElseThrow(EntityNotFoundException::new);
        if (!unit.getActive()) throw new IllegalArgumentException();
        Game game = unit.getGame();
        List<Unit> units = repository.findAllActiveByGameId(game.getId());
        if (units == null || units.isEmpty()) throw new EntityNotFoundException();
        AbstractUnitService service = serviceStrategies.get(unit.getType());
        units = dto.getCommand().performCommand(service, units, dto, game.getGridSize());
        repository.saveAll(units);
        logService.createLog(unit.getId() + ": " + dto.getCommand()
                + ": X: " + dto.getPositionX() + " | Y: " + dto.getPositionY(), game);
    }

    @Transactional
    public void performRandomCommand(UnitDTO dto) throws RuntimeException {
        Unit unit = repository.findByIdAndColor(dto.getId(), dto.getColor())
                .orElseThrow(EntityNotFoundException::new);
        if (!unit.getActive()) throw new IllegalArgumentException();
        Game game = unit.getGame();
        Random random = new Random();
        UnitCommandEnum[] commands = UnitCommandEnum.values();
        dto.setCommand(commands[random.nextInt(commands.length)]);
        dto.setPositionX(random.nextInt(game.getGridSize()));
        dto.setPositionY(random.nextInt(game.getGridSize()));
        performCommand(dto);
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
        List<UnitTypeEnum> types = new ArrayList<>() {{
            addAll(Collections.nCopies(archers, UnitTypeEnum.ARCHER));
            addAll(Collections.nCopies(transports, UnitTypeEnum.TRANSPORT));
            addAll(Collections.nCopies(cannons, UnitTypeEnum.CANNON));
        }};
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
