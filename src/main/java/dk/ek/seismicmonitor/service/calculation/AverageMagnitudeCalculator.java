package dk.ek.seismicmonitor.service.calculation;

import dk.ek.seismicmonitor.model.SensorReading;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AverageMagnitudeCalculator implements MagnitudeCalculator {

    @Override
    public double calculateAverageMagnitude(List<SensorReading> sensorReadings){
        return sensorReadings.stream()
                .mapToDouble(SensorReading::getEstimatedMagnitude)
                .average()
                .orElse(0);
    }
}
