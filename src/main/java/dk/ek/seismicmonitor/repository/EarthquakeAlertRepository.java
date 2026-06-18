package dk.ek.seismicmonitor.repository;

import dk.ek.seismicmonitor.model.EarthquakeAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EarthquakeAlertRepository extends JpaRepository<EarthquakeAlert, Long> {

}
