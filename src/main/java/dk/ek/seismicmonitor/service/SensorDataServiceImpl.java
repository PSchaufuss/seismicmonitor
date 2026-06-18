package dk.ek.seismicmonitor.service;

import dk.ek.seismicmonitor.dto.EarthquakeAlertResponseDTO;
import dk.ek.seismicmonitor.dto.SensorDataRequestDTO;
import dk.ek.seismicmonitor.dto.SensorReadingResponseDTO;

import dk.ek.seismicmonitor.model.AlertStatus;
import dk.ek.seismicmonitor.model.EarthquakeAlert;
import dk.ek.seismicmonitor.model.Sensor;
import dk.ek.seismicmonitor.model.SensorReading;

import dk.ek.seismicmonitor.repository.EarthquakeAlertRepository;
import dk.ek.seismicmonitor.repository.SensorReadingRepository;
import dk.ek.seismicmonitor.repository.SensorRepository;

import dk.ek.seismicmonitor.service.calculation.EpicenterEstimator;
import dk.ek.seismicmonitor.service.calculation.Location;
import dk.ek.seismicmonitor.service.calculation.LocationWithDistance;
import dk.ek.seismicmonitor.service.calculation.MagnitudeCalculator;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SensorDataServiceImpl implements SensorDataService {

    private final SensorRepository sensorRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final EarthquakeAlertRepository earthquakeAlertRepository;
    private final EpicenterEstimator epicenterEstimator;
    private final MagnitudeCalculator magnitudeCalculator;

    public SensorDataServiceImpl(SensorRepository sensorRepository, SensorReadingRepository sensorReadingRepository, EarthquakeAlertRepository earthquakeAlertRepository, EpicenterEstimator epicenterEstimator, MagnitudeCalculator magnitudeCalculator) {
        this.sensorRepository = sensorRepository;
        this.sensorReadingRepository = sensorReadingRepository;
        this.earthquakeAlertRepository = earthquakeAlertRepository;
        this.epicenterEstimator = epicenterEstimator;
        this.magnitudeCalculator = magnitudeCalculator;
    }

    @Override
    public void saveSensorData(List<SensorDataRequestDTO> sensorDataList) {

        List<SensorReading> savedReadingsFromThisRequest = new ArrayList<>();

        for (SensorDataRequestDTO dto : sensorDataList) {

            final double latitude = dto.getSensorLocation() != null
                    ? dto.getSensorLocation().getLatitude()
                    : 0.0;

            final double longitude = dto.getSensorLocation() != null
                    ? dto.getSensorLocation().getLongitude()
                    : 0.0;

            LocalDateTime recordedAt = null;

            try {
                recordedAt = LocalDateTime.parse(dto.getRecordedAt());
            } catch (Exception e) {

            }

            Sensor sensor = sensorRepository
                    .findBySensorId(dto.getSensorId())
                    .orElseGet(() -> new Sensor(
                            dto.getSensorId(),
                            latitude,
                            longitude
                    ));

            sensor = sensorRepository.save(sensor);

            SensorReading reading = new SensorReading(
                    dto.getReadingId(),
                    dto.getEstimatedDistanceToEpicenterKm(),
                    dto.getEstimatedMagnitude(),
                    recordedAt,
                    sensor
            );

            SensorReading savedReading = sensorReadingRepository.save(reading);
            savedReadingsFromThisRequest.add(savedReading);
        }

        List<SensorReading> validReadings = savedReadingsFromThisRequest.stream()
                .filter(this::isValidReading)
                .toList();

        if (validReadings.size() != 3) {
            return;
        }

        try {
            List<LocationWithDistance> measurements = validReadings.stream()
                    .map(reading -> new LocationWithDistance(
                            new Location(
                                    reading.getSensor().getLatitude(),
                                    reading.getSensor().getLongitude()
                            ),
                            reading.getEstimatedDistanceToEpicenterKm()
                    ))
                    .toList();

            Location epicenter = epicenterEstimator.estimate(measurements);
            double magnitude = magnitudeCalculator.calculateAverageMagnitude(validReadings);

            EarthquakeAlert alert = new EarthquakeAlert(
                    epicenter.latitude(),
                    epicenter.longitude(),
                    magnitude,
                    AlertStatus.UNDER_REVIEW
            );

            EarthquakeAlert savedAlert = earthquakeAlertRepository.save(alert);

            for (SensorReading reading : validReadings) {
                reading.setEarthquakeAlert(savedAlert);
                sensorReadingRepository.save(reading);
            }
        } catch (IllegalArgumentException e) {
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

    @Override
    public List<EarthquakeAlertResponseDTO> getAllEarthquakeAlerts() {
        return earthquakeAlertRepository.findAll()
                .stream()
                .map(alert -> new EarthquakeAlertResponseDTO(
                        alert.getId(),
                        alert.getEpicenterLatitude(),
                        alert.getEpicenterLongitude(),
                        alert.getEstimatedMagnitude(),
                        alert.getStatus(),
                        alert.getSensorReadings().size(),
                        alert.getUserReports().size()
                ))
                .toList();
    }

    private boolean isValidReading(SensorReading reading) {
        return reading.getSensor() != null
                && reading.getSensor().getLatitude() != 0
                && reading.getSensor().getLongitude() != 0
                && reading.getEstimatedDistanceToEpicenterKm() > 0
                && reading.getEstimatedMagnitude() > 0
                && reading.getRecordedAt() != null;
    }
}
