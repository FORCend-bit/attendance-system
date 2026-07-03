package com.example.attendancesystem.controller;

import com.example.attendancesystem.entity.Employee;
import com.example.attendancesystem.entity.EmployeeSalary;
import com.example.attendancesystem.service.BaseDataService;
import com.example.attendancesystem.service.EmployeeService;
import com.example.attendancesystem.vo.EmployeeDetailVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin/employee")
public class AdminEmployeeController {

    private final EmployeeService employeeService;
    private final BaseDataService baseDataService;

    public AdminEmployeeController(EmployeeService employeeService, BaseDataService baseDataService) {
        this.employeeService = employeeService;
        this.baseDataService = baseDataService;
    }

    @GetMapping("/list")
    public String list(@RequestParam(required = false) String employeeNo,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) Long departmentId,
                       @RequestParam(defaultValue = "1") int page,
                       Model model) {
        try {
            int size = 10;
            int total = employeeService.count(employeeNo, name, departmentId);
            model.addAttribute("employees", employeeService.list(employeeNo, name, departmentId, page, size));
            model.addAttribute("total", total);
            model.addAttribute("page", Math.max(page, 1));
            model.addAttribute("pages", Math.max((total + size - 1) / size, 1));
            model.addAttribute("departments", baseDataService.departments());
            model.addAttribute("employeeNo", employeeNo);
            model.addAttribute("name", name);
            model.addAttribute("departmentId", departmentId);
        } catch (Exception e) {
            model.addAttribute("error", friendly(e));
            model.addAttribute("employees", java.util.Collections.emptyList());
            model.addAttribute("departments", java.util.Collections.emptyList());
            model.addAttribute("total", 0);
            model.addAttribute("page", 1);
            model.addAttribute("pages", 1);
        }
        return "admin/employee/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        try {
            model.addAttribute("employee", new Employee());
            EmployeeSalary salary = new EmployeeSalary();
            salary.setBaseSalary(BigDecimal.ZERO);
            salary.setAllowance(BigDecimal.ZERO);
            salary.setEffectiveDate(LocalDate.now());
            model.addAttribute("salary", salary);
            addBaseData(model);
        } catch (Exception e) {
            model.addAttribute("error", friendly(e));
            model.addAttribute("employee", new Employee());
            model.addAttribute("salary", new EmployeeSalary());
            addEmptyBaseData(model);
        }
        return "admin/employee/form";
    }

    @PostMapping("/add")
    public String save(@ModelAttribute Employee employee, @ModelAttribute EmployeeSalary salary,
                       RedirectAttributes redirectAttributes) {
        try {
            employeeService.save(employee, salary);
            redirectAttributes.addFlashAttribute("message", "员工已新增");
            return "redirect:/admin/employee/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", friendly(e));
            return "redirect:/admin/employee/add";
        }
    }

    @GetMapping("/edit")
    public String edit(@RequestParam Long id, Model model) {
        try {
            EmployeeDetailVO detail = employeeService.detailById(id);
            model.addAttribute("detail", detail);
            model.addAttribute("employee", toEmployee(detail));
            model.addAttribute("salary", toSalary(detail));
            addBaseData(model);
        } catch (Exception e) {
            model.addAttribute("error", friendly(e));
            model.addAttribute("employee", new Employee());
            model.addAttribute("salary", new EmployeeSalary());
            addEmptyBaseData(model);
        }
        return "admin/employee/form";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute Employee employee, @ModelAttribute EmployeeSalary salary,
                         RedirectAttributes redirectAttributes) {
        try {
            employeeService.update(employee, salary);
            redirectAttributes.addFlashAttribute("message", "员工信息已保存");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", friendly(e));
        }
        return "redirect:/admin/employee/list";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam Long id, Model model) {
        try {
            model.addAttribute("employee", employeeService.detailById(id));
        } catch (Exception e) {
            model.addAttribute("error", friendly(e));
        }
        return "admin/employee/detail";
    }

    @PostMapping("/resign")
    public String resign(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.resign(id);
            redirectAttributes.addFlashAttribute("message", "员工已注销，数据仍保留");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", friendly(e));
        }
        return "redirect:/admin/employee/list";
    }

    private void addBaseData(Model model) {
        model.addAttribute("departments", baseDataService.departments());
        model.addAttribute("positions", baseDataService.positions());
        model.addAttribute("jobTitles", baseDataService.jobTitles());
    }

    private void addEmptyBaseData(Model model) {
        model.addAttribute("departments", java.util.Collections.emptyList());
        model.addAttribute("positions", java.util.Collections.emptyList());
        model.addAttribute("jobTitles", java.util.Collections.emptyList());
    }

    private Employee toEmployee(EmployeeDetailVO detail) {
        Employee employee = new Employee();
        if (detail == null) {
            return employee;
        }
        employee.setId(detail.getId());
        employee.setEmployeeNo(detail.getEmployeeNo());
        employee.setName(detail.getName());
        employee.setGender(detail.getGender());
        employee.setPhone(detail.getPhone());
        employee.setEmail(detail.getEmail());
        employee.setAddress(detail.getAddress());
        employee.setDepartmentId(detail.getDepartmentId());
        employee.setPositionId(detail.getPositionId());
        employee.setJobTitleId(detail.getJobTitleId());
        employee.setHireDate(detail.getHireDate());
        employee.setStatus(detail.getStatus());
        employee.setResignDate(detail.getResignDate());
        return employee;
    }

    private EmployeeSalary toSalary(EmployeeDetailVO detail) {
        EmployeeSalary salary = new EmployeeSalary();
        if (detail != null) {
            salary.setEmployeeId(detail.getId());
            salary.setBaseSalary(detail.getBaseSalary());
            salary.setAllowance(detail.getAllowance());
            salary.setEffectiveDate(detail.getEffectiveDate());
        }
        return salary;
    }

    private String friendly(Exception e) {
        return e.getMessage() == null ? "操作失败，请检查数据后重试" : e.getMessage();
    }
}
