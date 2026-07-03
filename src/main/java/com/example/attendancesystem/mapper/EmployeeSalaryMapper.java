package com.example.attendancesystem.mapper;

import com.example.attendancesystem.entity.EmployeeSalary;
import org.apache.ibatis.annotations.Param;

public interface EmployeeSalaryMapper {

    EmployeeSalary findByEmployeeId(@Param("employeeId") Long employeeId);

    int insert(EmployeeSalary salary);

    int updateByEmployeeId(EmployeeSalary salary);
}
