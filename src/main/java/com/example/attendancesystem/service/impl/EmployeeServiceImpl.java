package com.example.attendancesystem.service.impl;

import com.example.attendancesystem.entity.Employee;
import com.example.attendancesystem.entity.EmployeeSalary;
import com.example.attendancesystem.mapper.EmployeeMapper;
import com.example.attendancesystem.mapper.EmployeeSalaryMapper;
import com.example.attendancesystem.service.EmployeeService;
import com.example.attendancesystem.vo.EmployeeDetailVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final EmployeeSalaryMapper employeeSalaryMapper;

    public EmployeeServiceImpl(EmployeeMapper employeeMapper, EmployeeSalaryMapper employeeSalaryMapper) {
        this.employeeMapper = employeeMapper;
        this.employeeSalaryMapper = employeeSalaryMapper;
    }

    @Override
    public Employee findByEmployeeNo(String employeeNo) {
        if (employeeNo == null || employeeNo.isBlank()) {
            return null;
        }
        return employeeMapper.findByEmployeeNo(employeeNo.trim());
    }

    @Override
    public EmployeeDetailVO detailByEmployeeNo(String employeeNo) {
        if (employeeNo == null || employeeNo.isBlank()) {
            return null;
        }
        return employeeMapper.findDetailByEmployeeNo(employeeNo.trim());
    }

    @Override
    public EmployeeDetailVO detailById(Long id) {
        return id == null ? null : employeeMapper.findDetailById(id);
    }

    @Override
    public List<EmployeeDetailVO> list(String employeeNo, String name, Long departmentId, int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        List<EmployeeDetailVO> list = employeeMapper.findPage(clean(employeeNo), clean(name), departmentId, (safePage - 1) * safeSize, safeSize);
        return list == null ? Collections.emptyList() : list;
    }

    @Override
    public int count(String employeeNo, String name, Long departmentId) {
        return employeeMapper.count(clean(employeeNo), clean(name), departmentId);
    }

    @Override
    @Transactional
    public void save(Employee employee, EmployeeSalary salary) {
        prepareEmployee(employee);
        employeeMapper.insert(employee);
        if (salary != null) {
            prepareSalary(salary);
            salary.setEmployeeId(employee.getId());
            employeeSalaryMapper.insert(salary);
        }
    }

    @Override
    @Transactional
    public void update(Employee employee, EmployeeSalary salary) {
        prepareEmployee(employee);
        employeeMapper.update(employee);
        if (salary != null) {
            prepareSalary(salary);
            salary.setEmployeeId(employee.getId());
            EmployeeSalary old = employeeSalaryMapper.findByEmployeeId(employee.getId());
            if (old == null) {
                employeeSalaryMapper.insert(salary);
            } else {
                employeeSalaryMapper.updateByEmployeeId(salary);
            }
        }
    }

    @Override
    public void updateContact(String employeeNo, String phone, String email, String address) {
        Employee employee = findByEmployeeNo(employeeNo);
        if (employee == null) {
            throw new IllegalArgumentException("未找到员工工号：" + employeeNo);
        }
        employee.setPhone(phone);
        employee.setEmail(email);
        employee.setAddress(address);
        employeeMapper.updateContact(employee);
    }

    @Override
    public void resign(Long id) {
        if (id != null) {
            employeeMapper.resign(id);
        }
    }

    private void prepareEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("员工信息不能为空");
        }
        if (employee.getStatus() == null || employee.getStatus().isBlank()) {
            employee.setStatus("在职");
        }
    }

    private void prepareSalary(EmployeeSalary salary) {
        if (salary.getBaseSalary() == null) {
            salary.setBaseSalary(BigDecimal.ZERO);
        }
        if (salary.getAllowance() == null) {
            salary.setAllowance(BigDecimal.ZERO);
        }
        if (salary.getEffectiveDate() == null) {
            salary.setEffectiveDate(LocalDate.now());
        }
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
