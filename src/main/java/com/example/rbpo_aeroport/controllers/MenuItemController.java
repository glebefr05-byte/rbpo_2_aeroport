package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.models.MenuItem;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menuitems")
public class MenuItemController {
    private final List<MenuItem> menuItems = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @GetMapping("/{id}")
    public MenuItem getMenuItemById(@PathVariable Long id) {
        return menuItems.stream()
                .filter(MenuItem -> MenuItem.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    public MenuItem createMenuItem(@RequestBody MenuItem MenuItem) {
        MenuItem newMenuItem = MenuItem.builder()
                .id(idCounter.getAndIncrement())
                .name(MenuItem.getName())
                .positions(MenuItem.getPositions())
                .build();
        menuItems.add(newMenuItem);
        return newMenuItem;
    }

    @DeleteMapping("/{id}")
    public boolean deleteMenuItem(@PathVariable Long id) {
        menuItems.removeIf(MenuItem -> MenuItem.getId().equals(id));
        return true;
    }

    @PutMapping("/{id}")
    public MenuItem updateMenuItem(@PathVariable Long id, @RequestBody MenuItem MenuItem) {
        deleteMenuItem(id);
        MenuItem updatedMenuItem = MenuItem.builder()
                .id(id)
                .name(MenuItem.getName())
                .positions(MenuItem.getPositions())
                .build();
        menuItems.add(updatedMenuItem);
        return updatedMenuItem;
    }
}