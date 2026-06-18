package dk.ek.seismicmonitor.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sensorId;

    private double latitude;
    private double longitude;

    @OneToMany(mappedBy = "sensor")
    private List<SensorReading> readings = new ArrayList<>();

    public Sensor(){}

    public Sensor(String sensorId, double latitude, double longitude) {
        this.sensorId = sensorId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId(){
        return id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<SensorReading> getReadings() {
        return readings;
    }

    public void setReadings(List<SensorReading> readings) {
        this.readings = readings;
    }

}
