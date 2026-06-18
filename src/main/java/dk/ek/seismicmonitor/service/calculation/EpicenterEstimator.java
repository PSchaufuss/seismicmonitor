package dk.ek.seismicmonitor.service.calculation;

import java.util.List;

public interface EpicenterEstimator {
    Location estimate(List<LocationWithDistance> measurements);
}
