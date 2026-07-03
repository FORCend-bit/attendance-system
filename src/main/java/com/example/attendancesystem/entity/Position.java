package com.example.attendancesystem.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Position {

    private Long id;

    private String positionName;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
