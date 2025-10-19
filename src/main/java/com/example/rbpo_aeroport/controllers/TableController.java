package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.models.Table;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tables")
public class TableController {
    private final List<Table> tables = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @GetMapping("/{id}")
    public Table getTableById(@PathVariable Long id) {
        return tables.stream()
                .filter(Table -> Table.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    public Table createTable(@RequestBody Table Table) {
        Table newTable = Table.builder()
                .id(idCounter.getAndIncrement())
                .restaurantId(Table.getRestaurantId())
                .status(Table.getStatus())
                .build();
        tables.add(newTable);
        return newTable;
    }

    @DeleteMapping("/{id}")
    public boolean deleteTable(@PathVariable Long id) {
        tables.removeIf(Table -> Table.getId().equals(id));
        return true;
    }

    @PutMapping("/{id}")
    public Table updateTable(@PathVariable Long id, @RequestBody Table Table) {
        deleteTable(id);
        Table updatedTable = Table.builder()
                .id(id)
                .restaurantId(Table.getRestaurantId())
                .status(Table.getStatus())
                .build();
        tables.add(updatedTable);
        return updatedTable;
    }
}