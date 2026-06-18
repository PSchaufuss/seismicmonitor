package dk.ek.seismicmonitor.service;

import dk.ek.seismicmonitor.dto.EarthquakeAlertResponseDTO;
import dk.ek.seismicmonitor.dto.SensorReadingResponseDTO;
import dk.ek.seismicmonitor.dto.UserReportRequestDTO;
import dk.ek.seismicmonitor.dto.UserReportResponseDTO;
import dk.ek.seismicmonitor.model.AlertStatus;
import dk.ek.seismicmonitor.model.EarthquakeAlert;
import dk.ek.seismicmonitor.model.UserReport;
import dk.ek.seismicmonitor.repository.EarthquakeAlertRepository;
import dk.ek.seismicmonitor.repository.SensorReadingRepository;
import dk.ek.seismicmonitor.repository.UserReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EarthquakeAlertServiceImpl implements EarthquakeAlertService{

    private final EarthquakeAlertRepository earthquakeAlertRepository;
    private final UserReportRepository userReportRepository;
    private final SensorReadingRepository sensorReadingRepository;

    public EarthquakeAlertServiceImpl(EarthquakeAlertRepository earthquakeAlertRepository, UserReportRepository userReportRepository, SensorReadingRepository sensorReadingRepository){
        this.earthquakeAlertRepository = earthquakeAlertRepository;
        this.userReportRepository = userReportRepository;
        this.sensorReadingRepository = sensorReadingRepository;
    }

    @Override
    public List<EarthquakeAlertResponseDTO> getActiveAlerts(){
        return earthquakeAlertRepository.findByStatus(AlertStatus.ACTIVE)
                .stream()
                .map(alert -> new EarthquakeAlertResponseDTO(
                        alert.getId(),
                        alert.getEpicenterLatitude(),
                        alert.getEpicenterLongitude(),
                        alert.getEstimatedMagnitude(),
                        alert.getGeographicArea(),
                        alert.getStatus(),
                        alert.getSensorReadings().size(),
                        alert.getUserReports().size()
                ))
                .toList();
    }

    @Override
    public List<EarthquakeAlertResponseDTO> getAllAlerts(){
        return earthquakeAlertRepository.findAll()
                .stream()
                .map(alert -> new EarthquakeAlertResponseDTO(
                        alert.getId(),
                        alert.getEpicenterLatitude(),
                        alert.getEpicenterLongitude(),
                        alert.getEstimatedMagnitude(),
                        alert.getGeographicArea(),
                        alert.getStatus(),
                        alert.getSensorReadings().size(),
                        alert.getUserReports().size()
                ))
                .toList();
    }

    @Override
    public EarthquakeAlertResponseDTO updateAlertStatus(Long alertId, AlertStatus newStatus) {
        EarthquakeAlert alert = earthquakeAlertRepository
                .findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));

        if (!isValidTransition(alert.getStatus(), newStatus)){
            throw new IllegalArgumentException("Invalid state transition");
        }

        alert.setStatus(newStatus);

        EarthquakeAlert savedAlert = earthquakeAlertRepository.save(alert);

        return new EarthquakeAlertResponseDTO(
                savedAlert.getId(),
                savedAlert.getEpicenterLatitude(),
                savedAlert.getEpicenterLongitude(),
                savedAlert.getEstimatedMagnitude(),
                savedAlert.getGeographicArea(),
                savedAlert.getStatus(),
                savedAlert.getSensorReadings().size(),
                savedAlert.getUserReports().size()
        );
    }

    @Override
    public UserReportResponseDTO createUserReport(Long alertId, UserReportRequestDTO requestDTO) {
        EarthquakeAlert alert = earthquakeAlertRepository
                .findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));

        UserReport report = new UserReport(
                requestDTO.getIntensity(),
                LocalDateTime.now(),
                alert
        );

        UserReport savedReport = userReportRepository.save(report);

        return new UserReportResponseDTO(
                savedReport.getId(),
                savedReport.getIntensity(),
                savedReport.getReportedAt()
        );
    }

    @Override
    public List<UserReportResponseDTO> getUserReportsForAlert(Long alertId) {
        return userReportRepository.findByEarthquakeAlertId(alertId)
                .stream()
                .map(report -> new UserReportResponseDTO(
                        report.getId(),
                        report.getIntensity(),
                        report.getReportedAt()
                ))
                .toList();
    }

    @Override
    public List<SensorReadingResponseDTO> getSensorReadingsForAlert(Long alertId) {
        return sensorReadingRepository.findByEarthquakeAlertId(alertId)
                .stream()
                .map(reading -> new SensorReadingResponseDTO(
                        reading.getId(),
                        reading.getReadingId(),
                        reading.getSensor().getSensorId(),
                        reading.getSensor().getLatitude(),
                        reading.getSensor().getLongitude(),
                        reading.getEstimatedDistanceToEpicenterKm(),
                        reading.getEstimatedMagnitude(),
                        reading.getRecordedAt()
                ))
                .toList();
    }

    private boolean isValidTransition(AlertStatus currentStatus, AlertStatus newStatus) {

        return switch (currentStatus) {
            case UNDER_REVIEW ->
                    newStatus == AlertStatus.ACTIVE || newStatus == AlertStatus.FALSE_ALARM;
            case ACTIVE ->
                    newStatus == AlertStatus.NOT_ACTIVE;
            case FALSE_ALARM, NOT_ACTIVE ->
                    false;
        };
    }

}
