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

    private static final BigDecimal MAX_MONEY = new BigDecimal("99999999.99");

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
        prepareSalary(salary);
        employeeMapper.insert(employee);
        salary.setEmployeeId(employee.getId());
        employeeSalaryMapper.insert(salary);
    }

    @Override
    @Transactional
    public void update(Employee employee, EmployeeSalary salary) {
        prepareEmployee(employee);
        prepareSalary(salary);
        employeeMapper.update(employee);
        salary.setEmployeeId(employee.getId());
        EmployeeSalary old = employeeSalaryMapper.findByEmployeeId(employee.getId());
        if (old == null) {
            employeeSalaryMapper.insert(salary);
        } else {
            employeeSalaryMapper.updateByEmployeeId(salary);
        }
    }

    @Override
    public void updateContact(String employeeNo, String phone, String email, String address) {
        Employee employee = findByEmployeeNo(employeeNo);
        if (employee == null) {
            throw new IllegalArgumentException("\u672a\u627e\u5230\u5458\u5de5\u5de5\u53f7\uff1a" + employeeNo);
        }
        employee.setPhone(clean(phone));
        employee.setEmail(clean(email));
        employee.setAddress(clean(address));
        employeeMapper.updateContact(employee);
    }

    @Override
    public void resign(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("\u8bf7\u9009\u62e9\u9700\u8981\u6ce8\u9500\u7684\u5458\u5de5");
        }
        employeeMapper.resign(id);
    }

    private void prepareEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("\u5458\u5de5\u4fe1\u606f\u4e0d\u80fd\u4e3a\u7a7a");
        }
        employee.setEmployeeNo(clean(employee.getEmployeeNo()));
        employee.setName(clean(employee.getName()));
        employee.setGender(clean(employee.getGender()));
        employee.setPhone(clean(employee.getPhone()));
        employee.setEmail(clean(employee.getEmail()));
        employee.setAddress(clean(employee.getAddress()));
        employee.setStatus(clean(employee.getStatus()));

        if (employee.getEmployeeNo() == null) {
            throw new IllegalArgumentException("\u5458\u5de5\u5de5\u53f7\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (employee.getName() == null) {
            throw new IllegalArgumentException("\u5458\u5de5\u59d3\u540d\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (employee.getDepartmentId() == null) {
            throw new IllegalArgumentException("\u8bf7\u9009\u62e9\u6240\u5c5e\u90e8\u95e8");
        }
        if (employee.getPositionId() == null) {
            throw new IllegalArgumentException("\u8bf7\u9009\u62e9\u5c97\u4f4d");
        }
        if (employee.getJobTitleId() == null) {
            throw new IllegalArgumentException("\u8bf7\u9009\u62e9\u804c\u52a1");
        }
        if (employee.getHireDate() == null) {
            throw new IllegalArgumentException("\u5165\u804c\u65e5\u671f\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (employee.getStatus() == null) {
            employee.setStatus("\u5728\u804c");
        }
        if ("\u5728\u804c".equals(employee.getStatus())) {
            employee.setResignDate(null);
        }
    }

    private void prepareSalary(EmployeeSalary salary) {
        if (salary == null) {
            throw new IllegalArgumentException("\u85aa\u8d44\u4fe1\u606f\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (salary.getBaseSalary() == null) {
            salary.setBaseSalary(BigDecimal.ZERO);
        }
        if (salary.getAllowance() == null) {
            salary.setAllowance(BigDecimal.ZERO);
        }
        validateMoney(salary.getBaseSalary(), "\u57fa\u672c\u5de5\u8d44");
        validateMoney(salary.getAllowance(), "\u8865\u8d34");
        if (salary.getEffectiveDate() == null) {
            salary.setEffectiveDate(LocalDate.now());
        }
    }

    private void validateMoney(BigDecimal value, String fieldName) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(fieldName + "\u4e0d\u80fd\u4e3a\u8d1f\u6570");
        }
        if (value.compareTo(MAX_MONEY) > 0) {
            throw new IllegalArgumentException(fieldName + "\u4e0d\u80fd\u8d85\u8fc7 99999999.99");
        }
        if (value.scale() > 2) {
            throw new IllegalArgumentException(fieldName + "\u6700\u591a\u4fdd\u7559 2 \u4f4d\u5c0f\u6570");
        }
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
