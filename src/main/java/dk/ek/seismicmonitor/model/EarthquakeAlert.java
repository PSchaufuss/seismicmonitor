package dk.ek.seismicmonitor.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class EarthquakeAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double epicenterLatitude;
    private double epicenterLongitude;
    private double estimatedMagnitude;

    @Enumerated(EnumType.STRING)
    private AlertStatus status;

    @OneToMany(mappedBy = "earthquakeAlert")
    private List<SensorReading> sensorReadings = new ArrayList<>();

    public EarthquakeAlert(){}

    public EarthquakeAlert(double epicenterLatitude, double epicenterLongitude, double estimatedMagnitude, AlertStatus status) {
        this.epicenterLatitude = epicenterLatitude;
        this.epicenterLongitude = epicenterLongitude;
        this.estimatedMagnitude = estimatedMagnitude;
        this.status = status;
    }

    public Long getId(){
        return id;
    }

    public double getEpicenterLatitude() {
        return epicenterLatitude;
    }

    public void setEpicenterLatitude(double epicenterLatitude) {
        this.epicenterLatitude = epicenterLatitude;
    }

    public double getEpicenterLongitude() {
        return epicenterLongitude;
    }

    public void setEpicenterLongitude(double epicenterLongitude) {
        this.epicenterLongitude = epicenterLongitude;
    }

    public double getEstimatedMagnitude() {
        return estimatedMagnitude;
    }

    public void setEstimatedMagnitude(double estimatedMagnitude) {
        this.estimatedMagnitude = estimatedMagnitude;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    public List<SensorReading> getSensorReadings() {
        return sensorReadings;
    }

    public void setSensorReadings(List<SensorReading> sensorReadings) {
        this.sensorReadings = sensorReadings;
    }

}


