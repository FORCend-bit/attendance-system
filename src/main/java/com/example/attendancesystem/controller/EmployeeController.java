package com.example.attendancesystem.controller;

import com.example.attendancesystem.entity.Employee;
import com.example.attendancesystem.service.AttendanceService;
import com.example.attendancesystem.service.BusinessTripService;
import com.example.attendancesystem.service.EmployeeService;
import com.example.attendancesystem.service.LeaveService;
import com.example.attendancesystem.service.OvertimeService;
import com.example.attendancesystem.vo.EmployeeDetailVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;
    private final LeaveService leaveService;
    private final OvertimeService overtimeService;
    private final BusinessTripService businessTripService;

    public EmployeeController(EmployeeService employeeService, AttendanceService attendanceService,
                              LeaveService leaveService, OvertimeService overtimeService,
                              BusinessTripService businessTripService) {
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
        this.leaveService = leaveService;
        this.overtimeService = overtimeService;
        this.businessTripService = businessTripService;
    }

    @GetMapping({"", "/entry"})
    public String entry() {
        return "employee/entry";
    }

    @PostMapping("/enter")
    public String enter(@RequestParam(required = false) String employeeNo, RedirectAttributes redirectAttributes) {
        try {
            Employee employee = employeeService.findByEmployeeNo(employeeNo);
            if (employee == null) {
                redirectAttributes.addFlashAttribute("error", "未找到该工号，请确认后再试");
                return "redirect:/employee/entry";
            }
            return "redirect:/employee/home?employeeNo=" + employee.getEmployeeNo();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", friendly(e));
            return "redirect:/employee/entry";
        }
    }

    @GetMapping("/home")
    public String home(@RequestParam(required = false) String employeeNo, Model model) {
        addEmployee(model, employeeNo);
        return "employee/home";
    }

    @GetMapping("/profile")
    public String profile(@RequestParam(required = false) String employeeNo, Model model) {
        addEmployee(model, employeeNo);
        return "employee/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam String employeeNo, @RequestParam(required = false) String phone,
                                @RequestParam(required = false) String email, @RequestParam(required = false) String address,
                                RedirectAttributes redirectAttributes) {
        try {
            employeeService.updateContact(employeeNo, phone, email, address);
            redirectAttributes.addFlashAttribute("message", "个人信息已保存");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", friendly(e));
        }
        return "redirect:/employee/profile?employeeNo=" + employeeNo;
    }

    @GetMapping("/attendance")
    public String attendance(@RequestParam(required = false) String employeeNo, Model model) {
        addEmployee(model, employeeNo);
        try {
            model.addAttribute("records", attendanceService.listByEmployeeNo(employeeNo));
        } catch (Exception e) {
            model.addAttribute("records", Collections.emptyList());
            model.addAttribute("error", friendly(e));
        }
        return "employee/attendance";
    }

    @GetMapping("/leave")
    public String leave(@RequestParam(required = false) String employeeNo, Model model) {
        addEmployee(model, employeeNo);
        try {
            model.addAttribute("records", leaveService.listByEmployeeNo(employeeNo));
        } catch (Exception e) {
            model.addAttribute("records", Collections.emptyList());
            model.addAttribute("error", friendly(e));
        }
        return "employee/leave";
    }

    @GetMapping("/overtime")
    public String overtime(@RequestParam(required = false) String employeeNo, Model model) {
        addEmployee(model, employeeNo);
        try {
            model.addAttribute("records", overtimeService.listByEmployeeNo(employeeNo));
        } catch (Exception e) {
            model.addAttribute("records", Collections.emptyList());
            model.addAttribute("error", friendly(e));
        }
        return "employee/overtime";
    }

    @GetMapping("/trip")
    public String trip(@RequestParam(required = false) String employeeNo, Model model) {
        addEmployee(model, employeeNo);
        try {
            model.addAttribute("records", businessTripService.listByEmployeeNo(employeeNo));
        } catch (Exception e) {
            model.addAttribute("records", Collections.emptyList());
            model.addAttribute("error", friendly(e));
        }
        return "employee/trip";
    }

    private void addEmployee(Model model, String employeeNo) {
        try {
            EmployeeDetailVO employee = employeeService.detailByEmployeeNo(employeeNo);
            model.addAttribute("employee", employee);
            model.addAttribute("employeeNo", employeeNo);
            if (employee == null) {
                model.addAttribute("error", "未找到员工信息，请从员工端入口重新输入工号");
            }
        } catch (Exception e) {
            model.addAttribute("employee", null);
            model.addAttribute("employeeNo", employeeNo);
            model.addAttribute("error", friendly(e));
        }
    }

    private String friendly(Exception e) {
        return e.getMessage() == null ? "操作失败，请检查数据后重试" : e.getMessage();
    }
}
