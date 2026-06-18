package dk.ek.seismicmonitor.dto;

public class SensorDataRequestDTO {

    private String readingId;
    private String sensorId;
    private SensorLocationDTO sensorLocation;
    private Double estimatedDistanceToEpicenterKm;
    private Double estimatedMagnitude;
    private String recordedAt;

    public String getReadingId() {
        return readingId;
    }

    public void setReadingId(String readingId) {
        this.readingId = readingId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public SensorLocationDTO getSensorLocation() {
        return sensorLocation;
    }

    public void setSensorLocation(SensorLocationDTO sensorLocation) {
        this.sensorLocation = sensorLocation;
    }

    public Double getEstimatedDistanceToEpicenterKm() {
        return estimatedDistanceToEpicenterKm;
    }

    public void setEstimatedDistanceToEpicenterKm(Double estimatedDistanceToEpicenterKm) {
        this.estimatedDistanceToEpicenterKm = estimatedDistanceToEpicenterKm;
    }

    public Double getEstimatedMagnitude() {
        return estimatedMagnitude;
    }

    public void setEstimatedMagnitude(Double estimatedMagnitude) {
        this.estimatedMagnitude = estimatedMagnitude;
    }

    public String getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(String recordedAt) {
        this.recordedAt = recordedAt;
    }
}
