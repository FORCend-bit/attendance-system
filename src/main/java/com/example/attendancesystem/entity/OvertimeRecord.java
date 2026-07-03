package com.example.attendancesystem.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class OvertimeRecord {

    private Long id;

    private Long employeeId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate overtimeDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime;

    private BigDecimal hours;

    private String reason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
