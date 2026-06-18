package dk.ek.seismicmonitor.dto;

import dk.ek.seismicmonitor.model.AlertStatus;

public class EarthquakeAlertResponseDTO {

    private Long id;
    private double epicenterLatitude;
    private double epicenterLongitude;
    private double estimatedMagnitude;
    private AlertStatus status;
    private int sensorReadingCount;

    public EarthquakeAlertResponseDTO(Long id, double epicenterLatitude, double epicenterLongitude, double estimatedMagnitude, AlertStatus status, int sensorReadingCount) {
        this.id = id;
        this.epicenterLatitude = epicenterLatitude;
        this.epicenterLongitude = epicenterLongitude;
        this.estimatedMagnitude = estimatedMagnitude;
        this.status = status;
        this.sensorReadingCount = sensorReadingCount;
    }

    public Long getId() {
        return id;
    }

    public double getEpicenterLatitude() {
        return epicenterLatitude;
    }

    public double getEpicenterLongitude() {
        return epicenterLongitude;
    }

    public double getEstimatedMagnitude() {
        return estimatedMagnitude;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public int getSensorReadingCount() {
        return sensorReadingCount;
    }
}
