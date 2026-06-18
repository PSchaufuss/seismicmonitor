package dk.ek.seismicmonitor.service;

import dk.ek.seismicmonitor.repository.EarthquakeAlertRepository;
import dk.ek.seismicmonitor.repository.SensorReadingRepository;
import dk.ek.seismicmonitor.repository.SensorRepository;
import dk.ek.seismicmonitor.service.calculation.EpicenterEstimator;
import dk.ek.seismicmonitor.service.calculation.MagnitudeCalculator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dk.ek.seismicmonitor.dto.SensorDataRequestDTO;
import dk.ek.seismicmonitor.dto.SensorLocationDTO;
import dk.ek.seismicmonitor.model.AlertStatus;
import dk.ek.seismicmonitor.model.EarthquakeAlert;
import dk.ek.seismicmonitor.model.Sensor;
import dk.ek.seismicmonitor.model.SensorReading;
import dk.ek.seismicmonitor.service.calculation.Location;
import dk.ek.seismicmonitor.service.geocoding.ReverseGeocodingService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorDataServiceImplTest {

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private SensorReadingRepository sensorReadingRepository;

    @Mock
    private EarthquakeAlertRepository earthquakeAlertRepository;

    @Mock
    private EpicenterEstimator epicenterEstimator;

    @Mock
    private MagnitudeCalculator magnitudeCalculator;

    @Mock
    private ReverseGeocodingService reverseGeocodingService;

    @InjectMocks
    private SensorDataServiceImpl sensorDataService;

    @Test
    void saveSensorDataCreatesEarthquakeAlertWhenExactlyThreeValidReadings() {
        when(sensorRepository.findBySensorId(anyString())).thenReturn(Optional.empty());
        when(sensorRepository.save(any(Sensor.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(sensorReadingRepository.save(any(SensorReading.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(epicenterEstimator.estimate(anyList())).thenReturn(new Location(55.5, 12.5));
        when(magnitudeCalculator.calculateAverageMagnitude(anyList())).thenReturn(3.7);
        when(earthquakeAlertRepository.save(any(EarthquakeAlert.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(reverseGeocodingService.findGeographicArea(55.5, 12.5)).thenReturn("Copenhagen");

        List<SensorDataRequestDTO> request = List.of(
                createRequest("READ-001", "SEN-001", 55.1, 12.1, 40.0, 3.5),
                createRequest("READ-002", "SEN-002", 55.2, 12.2, 45.0, 3.8),
                createRequest("READ-003", "SEN-003", 55.3, 12.3, 50.0, 3.9)
        );

        sensorDataService.saveSensorData(request);

        ArgumentCaptor<EarthquakeAlert> alertCaptor = ArgumentCaptor.forClass(EarthquakeAlert.class);
        verify(earthquakeAlertRepository, times(1)).save(alertCaptor.capture());

        EarthquakeAlert savedAlert = alertCaptor.getValue();

        assertEquals(55.5, savedAlert.getEpicenterLatitude());
        assertEquals(12.5, savedAlert.getEpicenterLongitude());
        assertEquals(3.7, savedAlert.getEstimatedMagnitude());
        assertEquals("Copenhagen", savedAlert.getGeographicArea());
        assertEquals(AlertStatus.UNDER_REVIEW, savedAlert.getStatus());

        verify(epicenterEstimator, times(1)).estimate(anyList());
        verify(magnitudeCalculator, times(1)).calculateAverageMagnitude(anyList());
        verify(reverseGeocodingService, times(1)).findGeographicArea(55.5, 12.5);


    }

    @Test
    void saveSensorDataDoesNotCreateEarthquakeAlertWhenLessThanThreeValidReadings() {
        when(sensorRepository.findBySensorId(anyString())).thenReturn(Optional.empty());
        when(sensorRepository.save(any(Sensor.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(sensorReadingRepository.save(any(SensorReading.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<SensorDataRequestDTO> request = List.of(
                createRequest("READ-001", "SEN-001", 55.1, 12.1, 40.0, 3.5),
                createRequest("READ-002", "SEN-002", 55.2, 12.2, 45.0, 3.8)
        );

        sensorDataService.saveSensorData(request);

        verify(earthquakeAlertRepository, never()).save(any(EarthquakeAlert.class));
        verify(epicenterEstimator, never()).estimate(anyList());
        verify(magnitudeCalculator, never()).calculateAverageMagnitude(anyList());
    }

    @Test
    void saveSensorDataDoesNotCreateEarthquakeAlertWhenOneReadingIsInvalid() {
        when(sensorRepository.findBySensorId(anyString())).thenReturn(Optional.empty());
        when(sensorRepository.save(any(Sensor.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(sensorReadingRepository.save(any(SensorReading.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<SensorDataRequestDTO> request = List.of(
                createRequest("READ-001", "SEN-001", 55.1, 12.1, 40.0, 3.5),
                createRequest("READ-002", "SEN-002", 55.2, 12.2, -45.0, 3.8),
                createRequest("READ-003", "SEN-003", 55.3, 12.3, 50.0, 3.9)
        );

        sensorDataService.saveSensorData(request);

        verify(earthquakeAlertRepository, never()).save(any(EarthquakeAlert.class));
        verify(epicenterEstimator, never()).estimate(anyList());
        verify(magnitudeCalculator, never()).calculateAverageMagnitude(anyList());
    }

    @Test
    void saveSensorDataDoesNotCreateEarthquakeAlertWhenEpicenterCalculationFails() {
        when(sensorRepository.findBySensorId(anyString())).thenReturn(Optional.empty());
        when(sensorRepository.save(any(Sensor.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(sensorReadingRepository.save(any(SensorReading.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(epicenterEstimator.estimate(anyList()))
                .thenThrow(new IllegalArgumentException("No stable solution"));

        List<SensorDataRequestDTO> request = List.of(
                createRequest("READ-001", "SEN-001", 55.1, 12.1, 40.0, 3.5),
                createRequest("READ-002", "SEN-002", 55.2, 12.2, 45.0, 3.8),
                createRequest("READ-003", "SEN-003", 55.3, 12.3, 50.0, 3.9)
        );

        sensorDataService.saveSensorData(request);

        verify(sensorReadingRepository, times(3)).save(any(SensorReading.class));

        verify(earthquakeAlertRepository, never()).save(any(EarthquakeAlert.class));

        verify(magnitudeCalculator, never()).calculateAverageMagnitude(anyList());
    }

    @Test
    void saveSensorDataConnectsReadingsToCreatedEarthquakeAlert() {
        when(sensorRepository.findBySensorId(anyString())).thenReturn(Optional.empty());
        when(sensorRepository.save(any(Sensor.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(sensorReadingRepository.save(any(SensorReading.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(epicenterEstimator.estimate(anyList())).thenReturn(new Location(55.5, 12.5));
        when(magnitudeCalculator.calculateAverageMagnitude(anyList())).thenReturn(3.7);
        when(earthquakeAlertRepository.save(any(EarthquakeAlert.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(reverseGeocodingService.findGeographicArea(55.5, 12.5)).thenReturn("Copenhagen");

        List<SensorDataRequestDTO> request = List.of(
                createRequest("READ-001", "SEN-001", 55.1, 12.1, 40.0, 3.5),
                createRequest("READ-002", "SEN-002", 55.2, 12.2, 45.0, 3.8),
                createRequest("READ-003", "SEN-003", 55.3, 12.3, 50.0, 3.9)
        );

        sensorDataService.saveSensorData(request);

        ArgumentCaptor<SensorReading> readingCaptor = ArgumentCaptor.forClass(SensorReading.class);

        verify(sensorReadingRepository, times(6)).save(readingCaptor.capture());

        List<SensorReading> savedReadings = readingCaptor.getAllValues();

        SensorReading fourthSave = savedReadings.get(3);
        SensorReading fifthSave = savedReadings.get(4);
        SensorReading sixthSave = savedReadings.get(5);

        assertEquals("READ-001", fourthSave.getReadingId());
        assertEquals("READ-002", fifthSave.getReadingId());
        assertEquals("READ-003", sixthSave.getReadingId());

        assertEquals(true, fourthSave.getEarthquakeAlert() != null);
        assertEquals(true, fifthSave.getEarthquakeAlert() != null);
        assertEquals(true, sixthSave.getEarthquakeAlert() != null);
    }

    private SensorDataRequestDTO createRequest(String readingId, String sensorId, double latitude, double longitude, double distance, double magnitude) {
        SensorDataRequestDTO dto = new SensorDataRequestDTO();

        dto.setReadingId(readingId);
        dto.setSensorId(sensorId);
        dto.setSensorLocation(location(latitude, longitude));
        dto.setEstimatedDistanceToEpicenterKm(distance);
        dto.setEstimatedMagnitude(magnitude);
        dto.setRecordedAt("2026-05-20T10:15:30");

        return dto;
    }

    private SensorLocationDTO location(double latitude, double longitude) {
        SensorLocationDTO location = new SensorLocationDTO();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }


}