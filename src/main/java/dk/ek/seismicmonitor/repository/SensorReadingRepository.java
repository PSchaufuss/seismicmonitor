package dk.ek.seismicmonitor.repository;

import dk.ek.seismicmonitor.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long>{
}
