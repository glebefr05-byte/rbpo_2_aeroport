package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.entities.TableEntity;
import com.example.rbpo_aeroport.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    @Autowired
    private TableService tableService;

    @GetMapping
    public List<TableEntity> getAllTables() {
        return tableService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableEntity> getTableById(@PathVariable UUID id) {
        Optional<TableEntity> table = tableService.findById(id);
        return table.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<TableEntity> getTablesByRestaurant(@PathVariable UUID restaurantId) {
        return tableService.findByRestaurantId(restaurantId);
    }

    @PostMapping
    public TableEntity createTable(@RequestBody TableEntity table) {
        return tableService.save(table);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableEntity> updateTable(@PathVariable UUID id, @RequestBody TableEntity tableDetails) {
        try {
            TableEntity updatedTable = tableService.update(id, tableDetails);
            return ResponseEntity.ok(updatedTable);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TableEntity> partialUpdateTable(@PathVariable UUID id, @RequestBody TableEntity tableDetails) {
        try {
            TableEntity updatedTable = tableService.partialUpdate(id, tableDetails);
            return ResponseEntity.ok(updatedTable);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable UUID id) {
        if (tableService.findById(id).isPresent()) {
            tableService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeTableStatus(
            @PathVariable UUID id,
            @RequestBody ChangeStatusRequest request) {
        try {
            TableEntity updatedTable = tableService.changeTableStatus(id, request.getNewStatus());
            return ResponseEntity.ok(updatedTable);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    // DTO класс для запроса смены статуса
    public static class ChangeStatusRequest {
        private TableEntity.TableStatus newStatus;

        public TableEntity.TableStatus getNewStatus() { return newStatus; }
        public void setNewStatus(TableEntity.TableStatus newStatus) { this.newStatus = newStatus; }
    }
}