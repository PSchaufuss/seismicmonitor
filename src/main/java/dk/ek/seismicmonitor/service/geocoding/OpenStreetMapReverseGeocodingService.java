package dk.ek.seismicmonitor.service.geocoding;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

@Service
public class OpenStreetMapReverseGeocodingService implements ReverseGeocodingService {

    private final RestClient restClient;

    public OpenStreetMapReverseGeocodingService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://nominatim.openstreetmap.org")
                .defaultHeader("User-Agent", "SeismicMonitorExamProject")
                .build();
    }

    @Override
    public String findGeographicArea(double latitude, double longitude) {
        try {
            JsonNode response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/reverse")
                            .queryParam("format", "json")
                            .queryParam("lat", latitude)
                            .queryParam("lon", longitude)
                            .build())
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null || response.get("address") == null) {
                return "Unknown";
            }

            JsonNode address = response.get("address");

            if (address.get("city") != null) {
                return address.get("city").asText();
            }

            if (address.get("town") != null) {
                return address.get("town").asText();
            }

            if (address.get("village") != null) {
                return address.get("village").asText();
            }

            if (address.get("region") != null) {
                return address.get("region").asText();
            }

            if (address.get("country") != null) {
                return address.get("country").asText();
            }

            return "Unknown";

        } catch (Exception e) {
            return "Unknown";
        }
    }
}
