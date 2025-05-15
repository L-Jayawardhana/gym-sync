package com.example.Backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Backend.model.Appointment;
import com.example.Backend.model.TimeSlot;
import com.example.Backend.repository.AppointmentRepository;
import com.example.Backend.repository.TimeSlotRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private TimeSlotRepository timeSlotRepo;

    public Appointment bookAppointment(Appointment appointment) {
        // Validate input
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment object is null");
        }
        if (appointment.getId() == null) {
            throw new IllegalArgumentException("Appointment ID is null");
        }

        // Check if the time slot exists and is available
        Optional<TimeSlot> optionalSlot = timeSlotRepo.findByAppointmentId(appointment.getId());
        if (optionalSlot.isEmpty()) {
            throw new RuntimeException("Time slot not found for the given appointment ID");
        }

        TimeSlot slot = optionalSlot.get();
        if (slot.getStatus() != TimeSlot.SlotStatus.AVAILABLE) {
            throw new RuntimeException("Time slot is not available");
        }

        // Mark time slot as booked
        slot.setStatus(TimeSlot.SlotStatus.BOOKED);
        timeSlotRepo.save(slot);

        // Set appointment status to pending
        appointment.setStatus(Appointment.Status.PENDING);

        return appointmentRepo.save(appointment);
    }

    public List<Appointment> getAppointmentsForNextThreeDays() {
        List<Appointment> appointments = appointmentRepo.findAll();
        return appointments.stream()
                .filter(appointment -> {
                    Optional<TimeSlot> timeSlot = timeSlotRepo.findByAppointmentId(appointment.getId());
                    return timeSlot.isPresent() &&
                           !timeSlot.get().getDate().isBefore(LocalDate.now()) &&
                           !timeSlot.get().getDate().isAfter(LocalDate.now().plusDays(3));
                })
                .collect(Collectors.toList());
    }

    public List<TimeSlot> getAvailableSlotsForNextThreeDays() {
        LocalDate now = LocalDate.now();
        LocalDate end = now.plusDays(3);
        return timeSlotRepo.findByDateBetween(now, end);
    }

    public Appointment updateStatus(Long id, Appointment.Status status) {
        Optional<Appointment> optional = appointmentRepo.findById(id);
        if (optional.isPresent()) {
            Appointment app = optional.get();
            app.setStatus(status);
            return appointmentRepo.save(app);
        }
        return null;
    }
}