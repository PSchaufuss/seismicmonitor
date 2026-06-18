package dk.ek.seismicmonitor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public String health(){
        return "SeismicMonitor is up and running. :-)";
    }

}
