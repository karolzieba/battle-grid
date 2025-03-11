package pl.battlegrid.log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.battlegrid.game.Game;
import pl.battlegrid.unit.enums.UnitColorEnum;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private LogService logService;

    @Test
    void getGameLogs() {
        List<Log> logs = getObjects();
        List<LogDTO> logsDTOs = getDTOs(logs);
        when(logRepository.findAllByGameIdAndUnitsColor(anyLong(), any(UnitColorEnum.class))).thenReturn(logs);
        List<LogDTO> result = logService.getGameLogs(1L, UnitColorEnum.WHITE);
        assertNotNull(result);
        assertEquals(logsDTOs.get(0).getCommand(), result.get(0).getCommand());
    }

    @Test
    void createLog() {
        when(logRepository.save(any(Log.class))).thenReturn(any(Log.class));
        logService.createLog("command1", new Game());
        verify(logRepository).save(any(Log.class));
    }

    private Log getObject(Long id, String command) {
        return new Log() {{
            setId(id);
            setCommand(command);
        }};
    }

    private List<Log> getObjects() {
        return List.of(
                getObject(1L, "command1"),
                getObject(2L, "command2")
        );
    }

    private List<LogDTO> getDTOs(List<Log> logs) {
        return logs.stream()
                .map(log -> new LogDTO() {{
                    setCommand(log.getCommand());
                }})
                .collect(Collectors.toList());
    }
}