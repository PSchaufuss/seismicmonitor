package dk.ek.seismicmonitor.dto;

import java.time.LocalDateTime;

public class SensorReadingResponseDTO {

    private Long id;
    private String readingId;
    private String sensorId;
    private double latitude;
    private double longitude;
    private double estimatedDistanceToEpicenterKm;
    private double estimatedMagnitude;
    private LocalDateTime recordedAt;

    public SensorReadingResponseDTO(Long id, String readingId, String sensorId, double latitude, double longitude, double estimatedDistanceToEpicenterKm, double estimatedMagnitude, LocalDateTime recordedAt){
        this.id = id;
        this.readingId = readingId;
        this.sensorId = sensorId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.estimatedDistanceToEpicenterKm = estimatedDistanceToEpicenterKm;
        this.estimatedMagnitude = estimatedMagnitude;
        this.recordedAt = recordedAt;
    }

    public Long getId() {
        return id;
    }

    public String getReadingId() {
        return readingId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getEstimatedDistanceToEpicenterKm() {
        return estimatedDistanceToEpicenterKm;
    }

    public double getEstimatedMagnitude() {
        return estimatedMagnitude;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }
}
