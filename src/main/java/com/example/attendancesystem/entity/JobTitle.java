package com.example.attendancesystem.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobTitle {

    private Long id;

    private String titleName;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
