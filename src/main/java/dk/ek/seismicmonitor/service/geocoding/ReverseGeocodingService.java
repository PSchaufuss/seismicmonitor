package dk.ek.seismicmonitor.service.geocoding;

public interface ReverseGeocodingService {

    String findGeographicArea(double latitude, double longitude);
}
