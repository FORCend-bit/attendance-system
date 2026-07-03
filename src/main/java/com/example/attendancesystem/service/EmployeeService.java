package com.example.attendancesystem.service;

import com.example.attendancesystem.entity.Employee;
import com.example.attendancesystem.entity.EmployeeSalary;
import com.example.attendancesystem.vo.EmployeeDetailVO;

import java.util.List;

public interface EmployeeService {

    Employee findByEmployeeNo(String employeeNo);

    EmployeeDetailVO detailByEmployeeNo(String employeeNo);

    EmployeeDetailVO detailById(Long id);

    List<EmployeeDetailVO> list(String employeeNo, String name, Long departmentId, int page, int size);

    int count(String employeeNo, String name, Long departmentId);

    void save(Employee employee, EmployeeSalary salary);

    void update(Employee employee, EmployeeSalary salary);

    void updateContact(String employeeNo, String phone, String email, String address);

    void resign(Long id);
}
