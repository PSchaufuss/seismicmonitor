package dk.ek.seismicmonitor.controller;

import dk.ek.seismicmonitor.configuration.SecurityConfig;
import dk.ek.seismicmonitor.dto.EarthquakeAlertResponseDTO;
import dk.ek.seismicmonitor.dto.UserReportResponseDTO;
import dk.ek.seismicmonitor.model.AlertStatus;
import dk.ek.seismicmonitor.service.EarthquakeAlertService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EarthquakeAlertController.class)
@Import(SecurityConfig.class)
public class EarthquakeAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EarthquakeAlertService earthquakeAlertService;

    @Test
    public void getAllAlerts_Success() throws Exception {
        EarthquakeAlertResponseDTO alert = new EarthquakeAlertResponseDTO(
                1L,
                55.6761,
                12.5683,
                4.3,
                "Copenhagen",
                AlertStatus.UNDER_REVIEW,
                3,
                0
        );

        when(earthquakeAlertService.getAllAlerts())
                .thenReturn(List.of(alert));

        mockMvc.perform(get("/api/earthquake-alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("UNDER_REVIEW"))
                .andExpect(jsonPath("$[0].sensorReadingCount").value(3))
                .andExpect(jsonPath("$[0].geographicArea").value("Copenhagen"));
    }

    @Test
    public void getActiveAlerts_Success() throws Exception {
        EarthquakeAlertResponseDTO alert = new EarthquakeAlertResponseDTO(
                1L,
                55.6761,
                12.5683,
                4.3,
                "Copenhagen",
                AlertStatus.ACTIVE,
                3,
                2
        );

        when(earthquakeAlertService.getActiveAlerts())
                .thenReturn(List.of(alert));

        mockMvc.perform(get("/api/earthquake-alerts/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[0].userReportCount").value(2))
                .andExpect(jsonPath("$[0].geographicArea").value("Copenhagen"));
    }

    @Test
    public void updateAlertStatus_Success() throws Exception {
        EarthquakeAlertResponseDTO alert = new EarthquakeAlertResponseDTO(
                1L,
                55.6761,
                12.5683,
                4.3,
                "Copenhagen",
                AlertStatus.ACTIVE,
                3,
                0
        );

        when(earthquakeAlertService.updateAlertStatus(1L, AlertStatus.ACTIVE))
                .thenReturn(alert);

        mockMvc.perform(patch("/api/earthquake-alerts/1/status")
                        .param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(earthquakeAlertService).updateAlertStatus(1L, AlertStatus.ACTIVE);
    }

    @Test
    public void createUserReport_Success() throws Exception {
        UserReportResponseDTO report = new UserReportResponseDTO(
                1L,
                5,
                LocalDateTime.of(2026, 6, 18, 18, 0)
        );

        when(earthquakeAlertService.createUserReport(eq(1L), any()))
                .thenReturn(report);

        String json = """
                {
                  "intensity": 5
                }
                """;

        mockMvc.perform(post("/api/earthquake-alerts/1/user-reports")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.intensity").value(5));

        verify(earthquakeAlertService).createUserReport(eq(1L), any());
    }
}