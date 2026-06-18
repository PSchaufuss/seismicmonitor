package dk.ek.seismicmonitor.service;

import dk.ek.seismicmonitor.model.AlertStatus;
import dk.ek.seismicmonitor.model.EarthquakeAlert;
import dk.ek.seismicmonitor.repository.EarthquakeAlertRepository;
import dk.ek.seismicmonitor.repository.SensorReadingRepository;
import dk.ek.seismicmonitor.repository.UserReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EarthquakeAlertServiceImplTest {

    @Mock
    private EarthquakeAlertRepository earthquakeAlertRepository;

    @Mock
    private UserReportRepository userReportRepository;

    @Mock
    private SensorReadingRepository sensorReadingRepository;

    @InjectMocks
    private EarthquakeAlertServiceImpl earthquakeAlertService;

    @Test
    void updateAlertStatusFromUnderReviewToActiveIsAllowed() {
        EarthquakeAlert alert = new EarthquakeAlert(55.0, 12.0, 3.5, AlertStatus.UNDER_REVIEW);

        when(earthquakeAlertRepository.findById(1L)).thenReturn(Optional.of(alert));
        when(earthquakeAlertRepository.save(any(EarthquakeAlert.class))).thenAnswer(invocation -> invocation.getArgument(0));

        earthquakeAlertService.updateAlertStatus(1L, AlertStatus.ACTIVE);

        assertEquals(AlertStatus.ACTIVE, alert.getStatus());
        verify(earthquakeAlertRepository).save(alert);
    }

    @Test
    void updateAlertStatusFromUnderReviewToFalseAlarmIsAllowed() {
        EarthquakeAlert alert = new EarthquakeAlert(55.0, 12.0, 3.5, AlertStatus.UNDER_REVIEW);

        when(earthquakeAlertRepository.findById(1L)).thenReturn(Optional.of(alert));
        when(earthquakeAlertRepository.save(any(EarthquakeAlert.class))).thenAnswer(invocation -> invocation.getArgument(0));

        earthquakeAlertService.updateAlertStatus(1L, AlertStatus.FALSE_ALARM);

        assertEquals(AlertStatus.FALSE_ALARM, alert.getStatus());
        verify(earthquakeAlertRepository).save(alert);
    }

    @Test
    void updateAlertStatusFromActiveToNotActiveIsAllowed() {
        EarthquakeAlert alert = new EarthquakeAlert(55.0, 12.0, 3.5, AlertStatus.ACTIVE);

        when(earthquakeAlertRepository.findById(1L)).thenReturn(Optional.of(alert));
        when(earthquakeAlertRepository.save(any(EarthquakeAlert.class))).thenAnswer(invocation -> invocation.getArgument(0));

        earthquakeAlertService.updateAlertStatus(1L, AlertStatus.NOT_ACTIVE);

        assertEquals(AlertStatus.NOT_ACTIVE, alert.getStatus());
        verify(earthquakeAlertRepository).save(alert);
    }

    @Test
    void updateAlertStatusFromFalseAlarmIsNotAllowed() {
        EarthquakeAlert alert = new EarthquakeAlert(55.0, 12.0, 3.5, AlertStatus.FALSE_ALARM);

        when(earthquakeAlertRepository.findById(1L)).thenReturn(Optional.of(alert));

        assertThrows(IllegalArgumentException.class,
                () -> earthquakeAlertService.updateAlertStatus(1L, AlertStatus.ACTIVE));

        verify(earthquakeAlertRepository, never()).save(any(EarthquakeAlert.class));
    }

    @Test
    void updateAlertStatusFromNotActiveIsNotAllowed() {
        EarthquakeAlert alert = new EarthquakeAlert(55.0, 12.0, 3.5, AlertStatus.NOT_ACTIVE);

        when(earthquakeAlertRepository.findById(1L)).thenReturn(Optional.of(alert));

        assertThrows(IllegalArgumentException.class,
                () -> earthquakeAlertService.updateAlertStatus(1L, AlertStatus.ACTIVE));

        verify(earthquakeAlertRepository, never()).save(any(EarthquakeAlert.class));
    }
}