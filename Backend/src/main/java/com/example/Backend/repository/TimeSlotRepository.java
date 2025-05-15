package com.example.Backend.repository;

import com.example.Backend.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByDateBetween(LocalDate start, LocalDate end);
    Optional<TimeSlot> findByAppointmentId(Long appointmentId);
}