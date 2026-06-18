package dk.ek.seismicmonitor.service;

import dk.ek.seismicmonitor.dto.SensorDataRequestDTO;
import dk.ek.seismicmonitor.dto.SensorReadingResponseDTO;

import java.util.List;

public interface SensorDataService {

    void saveSensorData(List<SensorDataRequestDTO> sensorDataList);

    List<SensorReadingResponseDTO> getAllSensorReadings();
}
