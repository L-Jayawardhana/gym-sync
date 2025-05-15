package com.example.Backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Trainer must not be null")
    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Staff trainer;

    @ManyToOne
    @JoinColumn(name = "trainee_id", nullable = true)
    private Staff trainee;

    @NotNull(message = "Status must not be null")
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING, ACCEPTED, REJECTED
    }

    // Default constructor
    public Appointment() {
    }

    // Parameterized constructor
    public Appointment(Long id, Staff trainer, Staff trainee, Status status) {
        this.id = id;
        this.trainer = trainer;
        this.trainee = trainee;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Staff getTrainer() {
        return trainer;
    }

    public void setTrainer(Staff trainer) {
        this.trainer = trainer;
    }

    public Staff getTrainee() {
        return trainee;
    }

    public void setTrainee(Staff trainee) {
        this.trainee = trainee;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTrainerId() {
        return trainer != null ? trainer.getNIC() : null;
    }
}
