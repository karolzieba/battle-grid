package pl.battlegrid.unit;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.battlegrid.unit.enums.UnitColorEnum;

@RequiredArgsConstructor
@RestController
@RequestMapping("/unit")
public class UnitController {

    private final UnitService service;

    @GetMapping("/{idGame}")
    public ResponseEntity<?> getGameUnits(@PathVariable Long idGame,
                                          @RequestParam UnitColorEnum color) {
        try {
            return new ResponseEntity<>(service.getGameUnits(idGame, color), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/command")
    public ResponseEntity<?> performCommand(@RequestBody UnitDTO dto) {
        try {
            service.performCommand(dto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/randomCommand")
    public ResponseEntity<?> performRandomCommand(@RequestBody UnitDTO dto) {
        try {
            service.performRandomCommand(dto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
