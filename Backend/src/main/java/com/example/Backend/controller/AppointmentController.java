package com.example.Backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Backend.model.Appointment;
import com.example.Backend.model.TimeSlot;
import com.example.Backend.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService service;

    @PostMapping
    public Appointment book(@RequestBody Appointment appointment) {
        // Log the incoming request payload for debugging
        System.out.println("Received appointment: " + appointment);
        
        if (appointment == null || appointment.getId() == null) {
            throw new IllegalArgumentException("Invalid appointment data: Appointment object or ID is null");
        }
        return service.bookAppointment(appointment);
    }

    @GetMapping("/upcoming")
    public List<Appointment> getUpcomingAppointments() {
        return service.getAppointmentsForNextThreeDays();
    }

    @GetMapping("/slots")
    public List<TimeSlot> getAvailableSlots() {
        return service.getAvailableSlotsForNextThreeDays();
    }

    @PutMapping("/{id}/status")
    public Appointment updateStatus(@PathVariable Long id, @RequestParam Appointment.Status status) {
        return service.updateStatus(id, status);
    }
}