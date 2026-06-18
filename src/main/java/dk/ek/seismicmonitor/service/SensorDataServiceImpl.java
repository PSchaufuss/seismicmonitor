package dk.ek.seismicmonitor.service;

import dk.ek.seismicmonitor.dto.SensorDataRequestDTO;
import dk.ek.seismicmonitor.dto.SensorReadingResponseDTO;
import dk.ek.seismicmonitor.model.Sensor;
import dk.ek.seismicmonitor.model.SensorReading;
import dk.ek.seismicmonitor.repository.SensorReadingRepository;
import dk.ek.seismicmonitor.repository.SensorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SensorDataServiceImpl implements SensorDataService {

    private final SensorRepository sensorRepository;
    private final SensorReadingRepository sensorReadingRepository;

    public SensorDataServiceImpl(SensorRepository sensorRepository, SensorReadingRepository sensorReadingRepository){
        this.sensorRepository = sensorRepository;
        this.sensorReadingRepository = sensorReadingRepository;
    }

    @Override
    public void saveSensorData(List<SensorDataRequestDTO> sensorDataList){

        for (SensorDataRequestDTO dto : sensorDataList){
            Sensor sensor = sensorRepository
                    .findBySensorId(dto.getSensorId())
                    .orElseGet(() -> new Sensor(
                            dto.getSensorId(),
                            dto.getSensorLocation().getLatitude(),
                            dto.getSensorLocation().getLongitude()
                    ));

            sensor = sensorRepository.save(sensor);

            SensorReading reading = new SensorReading(
                    dto.getReadingId(),
                    dto.getEstimatedDistanceToEpicenterKm(),
                    dto.getEstimatedMagnitude(),
                    LocalDateTime.parse(dto.getRecordedAt()), sensor
            );
            sensorReadingRepository.save(reading);
        }
    }

    @Override
    public List<SensorReadingResponseDTO> getAllSensorReadings(){
        return sensorReadingRepository.findAll()
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
}
