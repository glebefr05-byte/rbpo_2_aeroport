package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.entities.MenuItemEntity;
import com.example.rbpo_aeroport.services.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping
    public List<MenuItemEntity> getAllMenuItems() {
        return menuItemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemEntity> getMenuItemById(@PathVariable UUID id) {
        Optional<MenuItemEntity> menuItem = menuItemService.findById(id);
        return menuItem.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuItemEntity> getMenuItemsByRestaurant(@PathVariable UUID restaurantId) {
        return menuItemService.findByRestaurantId(restaurantId);
    }

    @PostMapping
    public MenuItemEntity createMenuItem(@RequestBody MenuItemEntity menuItem) {
        return menuItemService.save(menuItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemEntity> updateMenuItem(@PathVariable UUID id, @RequestBody MenuItemEntity menuItemDetails) {
        try {
            MenuItemEntity updatedMenuItem = menuItemService.update(id, menuItemDetails);
            return ResponseEntity.ok(updatedMenuItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MenuItemEntity> partialUpdateMenuItem(@PathVariable UUID id, @RequestBody MenuItemEntity menuItemDetails) {
        try {
            MenuItemEntity updatedMenuItem = menuItemService.partialUpdate(id, menuItemDetails);
            return ResponseEntity.ok(updatedMenuItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable UUID id) {
        if (menuItemService.findById(id).isPresent()) {
            menuItemService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/table/{tableId}")
    public ResponseEntity<?> getMenuForTable(@PathVariable UUID tableId) {
        try {
            List<MenuItemEntity> menuItems = menuItemService.getMenuForTable(tableId);
            return ResponseEntity.ok(menuItems);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }
}