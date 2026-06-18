package dk.ek.seismicmonitor.repository;

import dk.ek.seismicmonitor.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long>{
    List<SensorReading> findByEarthquakeAlertId(Long alertId);
}
