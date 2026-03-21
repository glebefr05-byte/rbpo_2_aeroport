package com.example.rbpo_aeroport.models;

import com.example.rbpo_aeroport.entities.TableEntity.TableStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TableDto {
    // Getters and Setters
    private Long id;

    @NotNull
    private Long restaurantId;

    private TableStatus status;

    // Constructors
    public TableDto() {}

    public TableDto(Long id, Long restaurantId, TableStatus status) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.status = status;
    }

}