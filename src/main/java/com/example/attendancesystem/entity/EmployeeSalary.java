package com.example.attendancesystem.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EmployeeSalary {

    private Long id;

    private Long employeeId;

    private BigDecimal baseSalary;

    private BigDecimal allowance;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
