package com.example.attendancesystem.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Department {

    private Long id;

    private String departmentCode;

    private String departmentName;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
