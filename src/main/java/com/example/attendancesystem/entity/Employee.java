package com.example.attendancesystem.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Employee {

    private Long id;

    private String employeeNo;

    private String name;

    private String gender;

    private String phone;

    private String email;

    private String address;

    private Long departmentId;

    private Long positionId;

    private Long jobTitleId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate resignDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
