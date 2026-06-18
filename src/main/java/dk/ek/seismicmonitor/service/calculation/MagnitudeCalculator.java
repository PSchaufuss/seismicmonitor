package dk.ek.seismicmonitor.service.calculation;

import dk.ek.seismicmonitor.model.SensorReading;

import java.util.List;

public interface MagnitudeCalculator {
    double calculateAverageMagnitude(List<SensorReading> sensorReadings);
}
