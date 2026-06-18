package dk.ek.seismicmonitor.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UserReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int intensity;

    private LocalDateTime reportedAt;

    @ManyToOne
    private EarthquakeAlert earthquakeAlert;

    public UserReport() {
    }

    public UserReport(int intensity, LocalDateTime reportedAt, EarthquakeAlert earthquakeAlert) {
        this.intensity = intensity;
        this.reportedAt = reportedAt;
        this.earthquakeAlert = earthquakeAlert;
    }

    public Long getId() {
        return id;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(LocalDateTime reportedAt) {
        this.reportedAt = reportedAt;
    }

    public EarthquakeAlert getEarthquakeAlert() {
        return earthquakeAlert;
    }

    public void setEarthquakeAlert(EarthquakeAlert earthquakeAlert) {
        this.earthquakeAlert = earthquakeAlert;
    }
}