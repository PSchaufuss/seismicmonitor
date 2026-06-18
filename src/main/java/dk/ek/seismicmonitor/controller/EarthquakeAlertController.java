package dk.ek.seismicmonitor.controller;

import dk.ek.seismicmonitor.dto.EarthquakeAlertResponseDTO;
import dk.ek.seismicmonitor.dto.SensorReadingResponseDTO;
import dk.ek.seismicmonitor.dto.UserReportRequestDTO;
import dk.ek.seismicmonitor.dto.UserReportResponseDTO;
import dk.ek.seismicmonitor.model.AlertStatus;
import dk.ek.seismicmonitor.service.EarthquakeAlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/earthquake-alerts")
public class EarthquakeAlertController {

    private final EarthquakeAlertService earthquakeAlertService;

    public EarthquakeAlertController(EarthquakeAlertService earthquakeAlertService){
        this.earthquakeAlertService = earthquakeAlertService;
    }

    @GetMapping
    public List<EarthquakeAlertResponseDTO> getAllAlerts(){
        return earthquakeAlertService.getAllAlerts();
    }

    @GetMapping("/active")
    public List<EarthquakeAlertResponseDTO> getActiveAlerts(){
        return earthquakeAlertService.getActiveAlerts();
    }


    @PatchMapping("/{alertId}/status")
    public EarthquakeAlertResponseDTO updateAlertStatus(@PathVariable Long alertId,
                                                        @RequestParam AlertStatus status) {

        return earthquakeAlertService.updateAlertStatus(alertId, status);
    }

    @PostMapping("/{alertId}/user-reports")
    public UserReportResponseDTO createUserReport(@PathVariable Long alertId,
                                                  @RequestBody UserReportRequestDTO requestDTO){
        return earthquakeAlertService.createUserReport(alertId, requestDTO);
    }

    @GetMapping("/{alertId}/user-reports")
    public List<UserReportResponseDTO> getUserReportsForAlert(@PathVariable Long alertId){
        return earthquakeAlertService.getUserReportsForAlert(alertId);
    }

    @GetMapping("/{alertId}/sensor-readings")
    public List<SensorReadingResponseDTO> getSensorReadingsForAlert(@PathVariable Long alertId){
        return earthquakeAlertService.getSensorReadingsForAlert(alertId);
    }



}
