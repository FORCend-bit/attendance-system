package com.example.attendancesystem.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeDetailVO {

    private Long id;

    private String employeeNo;

    private String name;

    private String gender;

    private String phone;

    private String email;

    private String address;

    private Long departmentId;

    private String departmentName;

    private Long positionId;

    private String positionName;

    private Long jobTitleId;

    private String titleName;

    private LocalDate hireDate;

    private String status;

    private LocalDate resignDate;

    private BigDecimal baseSalary;

    private BigDecimal allowance;

    private LocalDate effectiveDate;
}
