package pl.battlegrid.log;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.battlegrid.game.Game;
import pl.battlegrid.unit.enums.UnitColorEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LogService {

    private final LogRepository repository;

    public List<LogDTO> getGameLogs(Long idGame, UnitColorEnum color) {
        List<Log> logs = repository.findAllByGameIdAndUnitsColor(idGame, color);
        if (logs == null || logs.isEmpty()) return new ArrayList<>();
        return logs.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void createLog(String command, Game game) {
        repository.save(new Log(command, game));
    }

    private LogDTO mapToDTO(Log log) {
        return new LogDTO() {{
            setCommand(log.getCommand());
            setCreatedAt(log.getCreatedAt());
        }};
    }
}
