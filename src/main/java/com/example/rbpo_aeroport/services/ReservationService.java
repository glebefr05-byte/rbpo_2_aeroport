package com.example.rbpo_aeroport.services;

import com.example.rbpo_aeroport.entities.CustomerEntity;
import com.example.rbpo_aeroport.entities.MenuItemEntity;
import com.example.rbpo_aeroport.entities.ReservationEntity;
import com.example.rbpo_aeroport.entities.TableEntity;
import com.example.rbpo_aeroport.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TableService tableService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MenuItemService menuItemService;

    public List<ReservationEntity> findAll() {
        return reservationRepository.findAll();
    }

    public Optional<ReservationEntity> findById(UUID id) {
        return reservationRepository.findById(id);
    }

    public List<ReservationEntity> findByCustomerId(UUID customerId) {
        return reservationRepository.findByCustomerId(customerId);
    }

    public List<ReservationEntity> findByTableId(UUID tableId) {
        return reservationRepository.findByTableId(tableId);
    }

    public List<ReservationEntity> findByStatus(ReservationEntity.ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    public Optional<ReservationEntity> findByIdAndCustomerId(UUID id, UUID customerId) {
        return reservationRepository.findByIdAndCustomerId(id, customerId);
    }

    public ReservationEntity save(ReservationEntity reservation) {
        return reservationRepository.save(reservation);
    }

    public ReservationEntity update(UUID id, ReservationEntity reservationDetails) {
        return reservationRepository.findById(id)
                .map(reservation -> {
                    reservation.setCustomer(reservationDetails.getCustomer());
                    reservation.setTable(reservationDetails.getTable());
                    reservation.setStartTime(reservationDetails.getStartTime());
                    reservation.setDuration(reservationDetails.getDuration());
                    reservation.setCost(reservationDetails.getCost());
                    reservation.setStatus(reservationDetails.getStatus());
                    return reservationRepository.save(reservation);
                })
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
    }

    public ReservationEntity partialUpdate(UUID id, ReservationEntity reservationDetails) {
        return reservationRepository.findById(id)
                .map(reservation -> {
                    if (reservationDetails.getCustomer() != null) {
                        reservation.setCustomer(reservationDetails.getCustomer());
                    }
                    if (reservationDetails.getTable() != null) {
                        reservation.setTable(reservationDetails.getTable());
                    }
                    if (reservationDetails.getStartTime() != null) {
                        reservation.setStartTime(reservationDetails.getStartTime());
                    }
                    if (reservationDetails.getDuration() != null) {
                        reservation.setDuration(reservationDetails.getDuration());
                    }
                    if (reservationDetails.getCost() != null) {
                        reservation.setCost(reservationDetails.getCost());
                    }
                    if (reservationDetails.getStatus() != null) {
                        reservation.setStatus(reservationDetails.getStatus());
                    }
                    return reservationRepository.save(reservation);
                })
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
    }

    public void deleteById(UUID id) {
        reservationRepository.deleteById(id);
    }

    @Transactional
    public int checkTableAvailability(UUID tableId, LocalDateTime startTime, Integer duration) {
        Optional<TableEntity> tableOpt = tableService.findById(tableId);
        if (tableOpt.isEmpty()) {
            return -1; // Столик не найден
        }

        TableEntity table = tableOpt.get();
        if (table.getStatus() != TableEntity.TableStatus.AVAILABLE) {
            return 0; // Столик не доступен по статусу
        }

        LocalDateTime endTime = startTime.plusMinutes(duration);
        List<ReservationEntity> confirmedReservations = reservationRepository.findByTableIdAndStatus(tableId, ReservationEntity.ReservationStatus.CONFIRMED);

        for (ReservationEntity reservation : confirmedReservations) {
            LocalDateTime resStart = reservation.getStartTime();
            LocalDateTime resEnd = resStart.plusMinutes(reservation.getDuration());

            // Проверка пересечения временных интервалов
            if (startTime.isBefore(resEnd) && endTime.isAfter(resStart)) {
                return 0; // Найдено пересечение
            }
        }

        return 1; // Столик свободен
    }

    @Transactional
    public ReservationEntity reserveTable(UUID tableId, UUID customerId, LocalDateTime startTime, Integer duration) {
        // Проверка доступности столика
        int availability = checkTableAvailability(tableId, startTime, duration);
        if (availability != 1) {
            throw new RuntimeException("Table is not available for reservation");
        }

        Optional<TableEntity> tableOpt = tableService.findById(tableId);
        Optional<CustomerEntity> customerOpt = customerService.findById(customerId);

        if (tableOpt.isEmpty() || customerOpt.isEmpty()) {
            throw new RuntimeException("Table or Customer not found");
        }

        TableEntity table = tableOpt.get();
        CustomerEntity customer = customerOpt.get();

        // Создание бронирования
        ReservationEntity reservation = new ReservationEntity();
        reservation.setCustomer(customer);
        reservation.setTable(table);
        reservation.setStartTime(startTime);
        reservation.setDuration(duration);
        reservation.setCost(duration * 1.0); // cost = duration
        reservation.setStatus(ReservationEntity.ReservationStatus.CONFIRMED);

        return reservationRepository.save(reservation);
    }

    @Transactional
    public ReservationEntity addPreOrder(UUID reservationId, UUID menuItemId, String positionName) {
        Optional<ReservationEntity> reservationOpt = reservationRepository.findById(reservationId);
        Optional<MenuItemEntity> menuItemOpt = menuItemService.findById(menuItemId);

        if (reservationOpt.isEmpty() || menuItemOpt.isEmpty()) {
            throw new RuntimeException("Reservation or MenuItem not found");
        }

        ReservationEntity reservation = reservationOpt.get();
        MenuItemEntity menuItem = menuItemOpt.get();

        // Поиск позиции в меню
        Double positionPrice = menuItem.getPositions().get(positionName);
        if (positionPrice == null) {
            throw new RuntimeException("Position not found in menu item");
        }

        // Увеличение стоимости бронирования
        reservation.setCost(reservation.getCost() + positionPrice);

        return reservationRepository.save(reservation);
    }
}