package dk.ek.seismicmonitor.dto;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public class UserReportResponseDTO {

    private Long id;
    private int intensity;
    private LocalDateTime reportedAt;

    public UserReportResponseDTO(Long id, int intensity, LocalDateTime reportedAt){
        this.id = id;
        this.intensity = intensity;
        this.reportedAt = reportedAt;
    }

    public Long getId(){
        return id;
    }

    public int getIntensity(){
        return intensity;
    }

    public LocalDateTime getReportedAt(){
        return reportedAt;
    }
}
