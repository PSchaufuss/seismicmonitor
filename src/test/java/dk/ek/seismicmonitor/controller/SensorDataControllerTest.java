package dk.ek.seismicmonitor.controller;

import dk.ek.seismicmonitor.configuration.SecurityConfig;
import dk.ek.seismicmonitor.dto.SensorReadingResponseDTO;
import dk.ek.seismicmonitor.service.SensorDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SensorDataController.class)
@Import(SecurityConfig.class)
public class SensorDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SensorDataService sensorDataService;

    @Test
    public void receiveSensorData_Success() throws Exception{
        String json = """
                [
                                  {
                                    "readingId": "reading-1",
                                    "sensorId": "sensor-a",
                                    "sensorLocation": {
                                      "latitude": 55.6761,
                                      "longitude": 12.5683
                                    },
                                    "estimatedDistanceToEpicenterKm": 25.5,
                                    "estimatedMagnitude": 4.3,
                                    "recordedAt": "2026-06-18T15:00:00"
                                  }
                                ]
                """;

        mockMvc.perform(post("/api/sensor-data")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());

        verify(sensorDataService).saveSensorData(org.mockito.ArgumentMatchers.anyList());
    }

    @Test
    public void getAllSensorReadings_Success() throws Exception{
        SensorReadingResponseDTO reading = new SensorReadingResponseDTO(
                1L,
                "reading-1",
                "sensor-a",
                55.6761,
                12.5683,
                25.5,
                4.3,
                LocalDateTime.of(2026, 6, 18, 15, 0)
        );

        when(sensorDataService.getAllSensorReadings())
                .thenReturn(List.of(reading));

        mockMvc.perform(get("/api/sensor-data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].readingId").value("reading-1"))
                .andExpect(jsonPath("$[0].sensorId").value("sensor-a"))
                .andExpect(jsonPath("$[0].estimatedMagnitude").value(4.3));
    }
}
