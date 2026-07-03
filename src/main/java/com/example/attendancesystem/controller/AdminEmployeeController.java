package com.example.attendancesystem.controller;

import com.example.attendancesystem.entity.Employee;
import com.example.attendancesystem.entity.EmployeeSalary;
import com.example.attendancesystem.service.BaseDataService;
import com.example.attendancesystem.service.EmployeeService;
import com.example.attendancesystem.vo.EmployeeDetailVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

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
            model.addAttribute("employees", Collections.emptyList());
            model.addAttribute("departments", Collections.emptyList());
            model.addAttribute("total", 0);
            model.addAttribute("page", 1);
            model.addAttribute("pages", 1);
        }
        return "admin/employee/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("salary", defaultSalary());
        addBaseDataSafely(model);
        return "admin/employee/form";
    }

    @PostMapping("/add")
    public String save(@ModelAttribute("employee") Employee employee,
                       BindingResult employeeBinding,
                       @ModelAttribute("salary") EmployeeSalary salary,
                       BindingResult salaryBinding,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (employeeBinding.hasErrors() || salaryBinding.hasErrors()) {
            model.addAttribute("error", "\u8868\u5355\u5185\u5bb9\u683c\u5f0f\u4e0d\u6b63\u786e\uff0c\u8bf7\u68c0\u67e5\u65e5\u671f\u548c\u91d1\u989d");
            addBaseDataSafely(model);
            return "admin/employee/form";
        }
        try {
            employeeService.save(employee, salary);
            redirectAttributes.addFlashAttribute("message", "\u5458\u5de5\u5df2\u65b0\u589e");
            return "redirect:/admin/employee/list";
        } catch (Exception e) {
            model.addAttribute("error", friendly(e));
            addBaseDataSafely(model);
            return "admin/employee/form";
        }
    }

    @GetMapping("/edit")
    public String edit(@RequestParam Long id, Model model) {
        try {
            EmployeeDetailVO detail = employeeService.detailById(id);
            if (detail == null) {
                throw new IllegalArgumentException("\u672a\u627e\u5230\u5458\u5de5\u4fe1\u606f");
            }
            model.addAttribute("employee", toEmployee(detail));
            model.addAttribute("salary", toSalary(detail));
            addBaseDataSafely(model);
        } catch (Exception e) {
            model.addAttribute("error", friendly(e));
            model.addAttribute("employee", new Employee());
            model.addAttribute("salary", defaultSalary());
            addBaseDataSafely(model);
        }
        return "admin/employee/form";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("employee") Employee employee,
                         BindingResult employeeBinding,
                         @ModelAttribute("salary") EmployeeSalary salary,
                         BindingResult salaryBinding,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (employeeBinding.hasErrors() || salaryBinding.hasErrors()) {
            model.addAttribute("error", "\u8868\u5355\u5185\u5bb9\u683c\u5f0f\u4e0d\u6b63\u786e\uff0c\u8bf7\u68c0\u67e5\u65e5\u671f\u548c\u91d1\u989d");
            addBaseDataSafely(model);
            return "admin/employee/form";
        }
        try {
            employeeService.update(employee, salary);
            redirectAttributes.addFlashAttribute("message", "\u5458\u5de5\u4fe1\u606f\u5df2\u4fdd\u5b58");
            return "redirect:/admin/employee/list";
        } catch (Exception e) {
            model.addAttribute("error", friendly(e));
            addBaseDataSafely(model);
            return "admin/employee/form";
        }
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
            redirectAttributes.addFlashAttribute("message", "\u5458\u5de5\u5df2\u6ce8\u9500\uff0c\u5386\u53f2\u6570\u636e\u4ecd\u4fdd\u7559");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", friendly(e));
        }
        return "redirect:/admin/employee/list";
    }

    private void addBaseDataSafely(Model model) {
        try {
            model.addAttribute("departments", baseDataService.departments());
            model.addAttribute("positions", baseDataService.positions());
            model.addAttribute("jobTitles", baseDataService.jobTitles());
        } catch (Exception e) {
            model.addAttribute("departments", Collections.emptyList());
            model.addAttribute("positions", Collections.emptyList());
            model.addAttribute("jobTitles", Collections.emptyList());
        }
    }

    private Employee toEmployee(EmployeeDetailVO detail) {
        Employee employee = new Employee();
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
        EmployeeSalary salary = defaultSalary();
        salary.setEmployeeId(detail.getId());
        if (detail.getBaseSalary() != null) {
            salary.setBaseSalary(detail.getBaseSalary());
        }
        if (detail.getAllowance() != null) {
            salary.setAllowance(detail.getAllowance());
        }
        if (detail.getEffectiveDate() != null) {
            salary.setEffectiveDate(detail.getEffectiveDate());
        }
        return salary;
    }

    private EmployeeSalary defaultSalary() {
        EmployeeSalary salary = new EmployeeSalary();
        salary.setBaseSalary(BigDecimal.ZERO);
        salary.setAllowance(BigDecimal.ZERO);
        salary.setEffectiveDate(LocalDate.now());
        return salary;
    }

    private String friendly(Exception e) {
        return e.getMessage() == null ? "\u64cd\u4f5c\u5931\u8d25\uff0c\u8bf7\u68c0\u67e5\u6570\u636e\u540e\u91cd\u8bd5" : e.getMessage();
    }
}
