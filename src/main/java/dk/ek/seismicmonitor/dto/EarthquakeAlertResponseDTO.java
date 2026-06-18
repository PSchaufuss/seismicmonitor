package dk.ek.seismicmonitor.dto;

import dk.ek.seismicmonitor.model.AlertStatus;

public class EarthquakeAlertResponseDTO {

    private Long id;
    private double epicenterLatitude;
    private double epicenterLongitude;
    private double estimatedMagnitude;
    private String geographicArea;
    private AlertStatus status;
    private int sensorReadingCount;
    private long userReportCount;

    public EarthquakeAlertResponseDTO(Long id, double epicenterLatitude, double epicenterLongitude, double estimatedMagnitude, String geographicArea, AlertStatus status, int sensorReadingCount, long userReportCount) {
        this.id = id;
        this.epicenterLatitude = epicenterLatitude;
        this.epicenterLongitude = epicenterLongitude;
        this.estimatedMagnitude = estimatedMagnitude;
        this.geographicArea = geographicArea;
        this.status = status;
        this.sensorReadingCount = sensorReadingCount;
        this.userReportCount = userReportCount;
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

    public String getGeographicArea() {
        return geographicArea;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public int getSensorReadingCount() {
        return sensorReadingCount;
    }

    public long getUserReportCount() {
        return userReportCount;
    }
}