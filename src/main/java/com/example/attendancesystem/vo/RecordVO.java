package com.example.attendancesystem.vo;

import lombok.Data;

@Data
public class RecordVO<T> {

    private T record;

    private String employeeNo;

    private String employeeName;

    private String departmentName;
}
