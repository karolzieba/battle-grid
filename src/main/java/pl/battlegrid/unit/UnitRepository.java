package pl.battlegrid.unit;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import pl.battlegrid.unit.enums.UnitColorEnum;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Unit> findAllActiveByGameId(Long idGame);
    List<Unit> findAllByGameIdAndColor(Long idGame, UnitColorEnum color);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Unit> findByIdAndColor(Long idUnit, UnitColorEnum color);
}
