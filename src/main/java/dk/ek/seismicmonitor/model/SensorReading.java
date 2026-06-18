package dk.ek.seismicmonitor.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String readingId;

    private double estimatedDistanceToEpicenterKm;
    private double estimatedMagnitude;

    private LocalDateTime recordedAt;

    @ManyToOne
    private Sensor sensor;

    @ManyToOne
    private EarthquakeAlert earthquakeAlert;

    public SensorReading(){}

    public SensorReading(String readingId, double estimatedDistanceToEpicenterKm, double estimatedMagnitude, LocalDateTime recordedAt, Sensor sensor){
        this.readingId = readingId;
        this.estimatedDistanceToEpicenterKm = estimatedDistanceToEpicenterKm;
        this.estimatedMagnitude = estimatedMagnitude;
        this.recordedAt = recordedAt;
        this.sensor = sensor;
    }

    public Long getId() {
        return id;
    }

    public String getReadingId() {
        return readingId;
    }

    public void setReadingId(String readingId) {
        this.readingId = readingId;
    }

    public double getEstimatedDistanceToEpicenterKm() {
        return estimatedDistanceToEpicenterKm;
    }

    public void setEstimatedDistanceToEpicenterKm(double estimatedDistanceToEpicenterKm) {
        this.estimatedDistanceToEpicenterKm = estimatedDistanceToEpicenterKm;
    }

    public double getEstimatedMagnitude() {
        return estimatedMagnitude;
    }

    public void setEstimatedMagnitude(double estimatedMagnitude) {
        this.estimatedMagnitude = estimatedMagnitude;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public EarthquakeAlert getEarthquakeAlert(){
        return earthquakeAlert;
    }

    public void setEarthquakeAlert(EarthquakeAlert earthquakeAlert){
        this.earthquakeAlert = earthquakeAlert;
    }
}
