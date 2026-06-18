package dk.ek.seismicmonitor.dto;

public class UserReportRequestDTO {

    private int intensity;

    public UserReportRequestDTO(){
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }
}
