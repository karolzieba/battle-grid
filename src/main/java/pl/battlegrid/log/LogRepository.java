package pl.battlegrid.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.battlegrid.unit.UnitColorEnum;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    @Query("SELECT l FROM Log l " +
            "JOIN l.game g " +
            "JOIN g.units u " +
            "WHERE g.id = :idGame AND u.color = :color")
    List<Log> findAllByGameIdAndUnitsColor(Long idGame, UnitColorEnum color);
}
