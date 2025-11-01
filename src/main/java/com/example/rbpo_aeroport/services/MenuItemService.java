package com.example.rbpo_aeroport.services;

import com.example.rbpo_aeroport.entities.MenuItemEntity;
import com.example.rbpo_aeroport.entities.TableEntity;
import com.example.rbpo_aeroport.repositories.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private TableService tableService;

    public List<MenuItemEntity> findAll() {
        return menuItemRepository.findAll();
    }

    public Optional<MenuItemEntity> findById(UUID id) {
        return menuItemRepository.findById(id);
    }

    public List<MenuItemEntity> findByRestaurantId(UUID restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    public MenuItemEntity save(MenuItemEntity menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public MenuItemEntity update(UUID id, MenuItemEntity menuItemDetails) {
        return menuItemRepository.findById(id)
                .map(menuItem -> {
                    menuItem.setName(menuItemDetails.getName());
                    menuItem.setPositions(menuItemDetails.getPositions());
                    menuItem.setRestaurant(menuItemDetails.getRestaurant());
                    return menuItemRepository.save(menuItem);
                })
                .orElseThrow(() -> new RuntimeException("MenuItem not found with id: " + id));
    }

    public MenuItemEntity partialUpdate(UUID id, MenuItemEntity menuItemDetails) {
        return menuItemRepository.findById(id)
                .map(menuItem -> {
                    if (menuItemDetails.getName() != null) {
                        menuItem.setName(menuItemDetails.getName());
                    }
                    if (menuItemDetails.getPositions() != null) {
                        menuItem.setPositions(menuItemDetails.getPositions());
                    }
                    if (menuItemDetails.getRestaurant() != null) {
                        menuItem.setRestaurant(menuItemDetails.getRestaurant());
                    }
                    return menuItemRepository.save(menuItem);
                })
                .orElseThrow(() -> new RuntimeException("MenuItem not found with id: " + id));
    }

    public void deleteById(UUID id) {
        menuItemRepository.deleteById(id);
    }

    public List<MenuItemEntity> getMenuForTable(UUID tableId) {
        Optional<TableEntity> tableOpt = tableService.findById(tableId);
        if (tableOpt.isEmpty()) {
            throw new RuntimeException("Table not found with id: " + tableId);
        }

        TableEntity table = tableOpt.get();
        UUID restaurantId = table.getRestaurant().getId();

        return menuItemRepository.findByRestaurantId(restaurantId);
    }
}