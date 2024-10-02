package com.olyv.persistance;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "flights")
public class FlightStatus {

    @Id
    private String id;

    private String flight;

    private String status;

    private LocalDateTime created;

    private String createdBy;

    public FlightStatus(String flight, String status, LocalDateTime created, String createdBy) {
        this.flight = flight;
        this.status = status;
        this.created = created;
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public String getFlight() {
        return flight;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getCreatedBy() {
        return createdBy;
    }
}
