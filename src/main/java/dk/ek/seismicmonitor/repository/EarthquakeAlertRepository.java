package dk.ek.seismicmonitor.repository;

import dk.ek.seismicmonitor.model.AlertStatus;
import dk.ek.seismicmonitor.model.EarthquakeAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EarthquakeAlertRepository extends JpaRepository<EarthquakeAlert, Long> {

    List<EarthquakeAlert> findByStatus(AlertStatus status);
}
