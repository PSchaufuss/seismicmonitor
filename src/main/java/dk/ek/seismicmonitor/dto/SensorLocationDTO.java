package dk.ek.seismicmonitor.dto;

public class SensorLocationDTO {

    private double latitude;
    private double longitude;

    public Double getLatitude(){
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
