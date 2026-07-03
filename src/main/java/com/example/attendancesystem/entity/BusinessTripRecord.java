package com.example.attendancesystem.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BusinessTripRecord {

    private Long id;

    private Long employeeId;

    private String destination;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private BigDecimal days;

    private String reason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
