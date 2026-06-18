package dk.ek.seismicmonitor.service;

import dk.ek.seismicmonitor.dto.EarthquakeAlertResponseDTO;
import dk.ek.seismicmonitor.dto.SensorReadingResponseDTO;
import dk.ek.seismicmonitor.dto.UserReportRequestDTO;
import dk.ek.seismicmonitor.dto.UserReportResponseDTO;
import dk.ek.seismicmonitor.model.AlertStatus;

import java.util.List;

public interface EarthquakeAlertService {

    List<EarthquakeAlertResponseDTO> getActiveAlerts();

    List<EarthquakeAlertResponseDTO> getAllAlerts();

    EarthquakeAlertResponseDTO updateAlertStatus(Long alertId, AlertStatus newStatus);

    UserReportResponseDTO createUserReport(Long alertId, UserReportRequestDTO requestDTO);

    List<UserReportResponseDTO> getUserReportsForAlert(Long alertId);

    List<SensorReadingResponseDTO> getSensorReadingsForAlert(Long alertId);
}