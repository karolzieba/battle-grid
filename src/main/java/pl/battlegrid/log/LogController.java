package pl.battlegrid.log;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.battlegrid.unit.UnitColorEnum;

@RequiredArgsConstructor
@RestController
@RequestMapping("/log")
public class LogController {

    private final LogService service;

    @GetMapping("/{idGame}")
    public ResponseEntity<?> getGameLogs(@PathVariable Long idGame,
                                         @RequestParam UnitColorEnum color) {
        try {
            return new ResponseEntity<>(service.getGameLogs(idGame, color), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
