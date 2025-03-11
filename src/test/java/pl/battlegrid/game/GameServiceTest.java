package pl.battlegrid.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;
import pl.battlegrid.unit.UnitService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UnitService unitService;

    @InjectMocks
    private GameService gameService;

    @Test
    void getGames() {
        List<Game> games = getObjects();
        List<GameDTO> gamesDTOs = getDTOs(games);
        Page<Game> page = new PageImpl<>(games, PageRequest.of(0, 10), games.size());
        when(gameRepository.findAll(any(Pageable.class))).thenReturn(page);
        List<GameDTO> result = gameService.getGames(0);
        assertNotNull(result);
        assertEquals(gamesDTOs.get(1).getActive(), result.get(1).getActive());
    }

    @Test
    void createNewGameWithCorrectGridSize() {
        ReflectionTestUtils.setField(gameService, "gridSize", 8);
        when(unitService.createNewUnits(8)).thenReturn(new ArrayList<>());
        when(gameRepository.save(any(Game.class))).thenReturn(getObject(1L, false));
        Long idGame = gameService.createNewGame();
        assertNotNull(idGame);
        assertEquals(1L, idGame);
    }

    @Test
    void createNewGameWithInCorrectGridSize() {
        ReflectionTestUtils.setField(gameService, "gridSize", 1);
        assertThrows(IllegalArgumentException.class, () -> gameService.createNewGame());
    }

    private Game getObject(Long id, Boolean active) {
        return new Game() {{
            setId(id);
            setActive(active);
        }};
    }

    private List<Game> getObjects() {
        return List.of(
                getObject(1L, false),
                getObject(2L, true)
        );
    }

    private List<GameDTO> getDTOs(List<Game> games) {
        return games.stream()
                .map(game -> new GameDTO() {{
                    setId(game.getId());
                    setActive(game.getActive());
                }})
                .collect(Collectors.toList());
    }
}