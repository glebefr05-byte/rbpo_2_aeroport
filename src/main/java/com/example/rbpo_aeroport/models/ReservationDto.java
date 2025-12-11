package com.example.rbpo_aeroport.models;

import com.example.rbpo_aeroport.entities.ReservationEntity.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ReservationDto {
    private Long id;

    @NotNull
    private Long customerId;

    @NotNull
    private Long tableId;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private Integer duration;

    @NotNull
    private Double cost;

    private ReservationStatus status;

    // Constructors
    public ReservationDto() {}

    public ReservationDto(Long id, Long customerId, Long tableId, LocalDateTime startTime,
                          Integer duration, Double cost, ReservationStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.tableId = tableId;
        this.startTime = startTime;
        this.duration = duration;
        this.cost = cost;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getTableId() { return tableId; }
    public void setTableId(Long tableId) { this.tableId = tableId; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
}