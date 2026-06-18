package dk.ek.seismicmonitor.controller;

import dk.ek.seismicmonitor.dto.EarthquakeAlertResponseDTO;
import dk.ek.seismicmonitor.dto.SensorDataRequestDTO;
import dk.ek.seismicmonitor.dto.SensorReadingResponseDTO;
import dk.ek.seismicmonitor.model.EarthquakeAlert;
import dk.ek.seismicmonitor.service.SensorDataService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensor-data")
public class SensorDataController {

    private final SensorDataService sensorDataService;

    public SensorDataController(SensorDataService sensorDataService){
        this.sensorDataService = sensorDataService;
    }

    @PostMapping
    public void receiveSensorData(@RequestBody List<SensorDataRequestDTO> sensorDataList){
        sensorDataService.saveSensorData(sensorDataList);
    }

    @GetMapping
    public List<SensorReadingResponseDTO> getAllSensorReadings(){
        return sensorDataService.getAllSensorReadings();
    }

    @GetMapping("/earthquake-alerts")
    public List<EarthquakeAlertResponseDTO> getAllEarthquakeAlerts(){
        return sensorDataService.getAllEarthquakeAlerts();
    }
}
