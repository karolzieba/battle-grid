package pl.battlegrid.game;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.battlegrid.unit.UnitService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GameService {

    private final GameRepository repository;
    private final UnitService unitService;

    private static final Integer PAGE_SIZE = 10;
    private static final Sort.Direction PAGE_SORT_DIRECTION = Sort.Direction.DESC;
    private static final Integer MINIMAL_GRID_SIZE = 5;
    private static final Integer MAXIMAL_GRID_SIZE = 10;

    @Value("${game.settings.grid-size}")
    private Integer gridSize;

    public List<GameDTO> getGames(Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE, PAGE_SORT_DIRECTION);
        Page<Game> games = repository.findAll(pageRequest);
        if (games.isEmpty()) return new ArrayList<>();
        return games.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public long createNewGame() throws IllegalArgumentException {
        isGridSizeCorrect();
        deactivatePreviousGame();
        Game game = new Game(gridSize, unitService.createNewUnits(gridSize));
        game = repository.save(game);
        return game.getId();
    }

    private GameDTO mapToDTO(Game game) {
        return new GameDTO() {{
            setId(game.getId());
            setGridSize(game.getGridSize());
            setActive(game.getActive());
            setCreatedAt(game.getCreatedAt());
        }};
    }

    private void isGridSizeCorrect() throws IllegalArgumentException {
        if (gridSize < MINIMAL_GRID_SIZE || gridSize > MAXIMAL_GRID_SIZE) throw new IllegalArgumentException();
    }

    private void deactivatePreviousGame() {
        Game game = repository.findByActiveTrue()
                .orElse(null);
        if (game != null) {
            game.setActive(false);
            repository.save(game);
        }
    }
}
