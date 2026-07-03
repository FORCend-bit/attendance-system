package com.example.attendancesystem.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyStatisticsVO {

    private String yearMonth;

    private Long departmentId;

    private String departmentName;

    private Integer employeeCount;

    private Integer normalCount;

    private Integer lateCount;

    private Integer absentCount;

    private BigDecimal leaveDays;

    private BigDecimal overtimeHours;

    private BigDecimal tripDays;
}
