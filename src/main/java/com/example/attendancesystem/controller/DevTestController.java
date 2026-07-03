package com.example.attendancesystem.controller;

import com.example.attendancesystem.entity.Department;
import com.example.attendancesystem.entity.Employee;
import com.example.attendancesystem.mapper.DepartmentMapper;
import com.example.attendancesystem.mapper.EmployeeMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dev")
public class DevTestController {

    private static final String TEST_EMPLOYEE_NO = "E2026001";

    private final DepartmentMapper departmentMapper;
    private final EmployeeMapper employeeMapper;

    public DevTestController(DepartmentMapper departmentMapper, EmployeeMapper employeeMapper) {
        this.departmentMapper = departmentMapper;
        this.employeeMapper = employeeMapper;
    }

    @GetMapping("/db-test")
    public Map<String, Object> dbTest() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("testEmployeeNo", TEST_EMPLOYEE_NO);

        try {
            List<Department> departments = departmentMapper.selectAll();
            if (departments == null) {
                departments = Collections.emptyList();
            }

            Employee employee = employeeMapper.selectByEmployeeNo(TEST_EMPLOYEE_NO);

            result.put("success", true);
            result.put("message", "数据库连接成功");
            result.put("departmentCount", departments.size());
            result.put("departmentTip", departments.isEmpty() ? "部门列表为空，但接口正常返回" : "部门列表查询成功");
            result.put("departments", departments);
            result.put("employee", employee);
            result.put("employeeTip", employee == null ? "未找到测试员工 E2026001" : "测试员工查询成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "数据库连接或查询失败，但接口已正常返回");
            result.put("departmentCount", 0);
            result.put("departmentTip", "部门列表查询失败或为空");
            result.put("departments", Collections.emptyList());
            result.put("employee", null);
            result.put("employeeTip", "未找到测试员工 E2026001");
            result.put("errorType", e.getClass().getSimpleName());
            result.put("errorMessage", e.getMessage());
        }

        return result;
    }
}
