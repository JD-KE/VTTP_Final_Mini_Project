package com.jd.eventhall.MainAppBackend.service;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class IGDBAppHealthService {
    RestTemplate template = new RestTemplate();

    @Autowired
    private MeterRegistry meterRegistry;
    
    @PostConstruct
    public void buildGauge () {
        Gauge.builder("local_IGDB_app_health", () ->(getlocalIGDBAppStatus()))
            .description("""
                Health of local app that manages local IGDB database, 
                1 for UP, 0 for DOWN or other issues. Custom metric.""")
            .register(meterRegistry);
    }

    @Value("${igdb.application.url}")
    private String localIGDBAppUrl;

    public int getlocalIGDBAppStatus() {
        String uri = localIGDBAppUrl.concat("/actuator/health");

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = template.getForEntity(uri, String.class);
        } catch (Exception e) {
            return 0;
        }
        

        JsonReader jsonReader = Json.createReader(new StringReader(responseEntity.getBody()));
        
        JsonObject jsonObject = jsonReader.readObject();

        String status = jsonObject.getString("status");

        if(status.equals("UP")) {
            return 1;
        } else {
            return 0;
        }
    }

    
}
