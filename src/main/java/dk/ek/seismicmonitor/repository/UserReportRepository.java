package dk.ek.seismicmonitor.repository;

import dk.ek.seismicmonitor.model.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    List<UserReport> findByEarthquakeAlertId(Long alertId);

    long countByEarthquakeAlertId(Long alertId);
}
