package com.example.attendancesystem.controller;

import com.example.attendancesystem.service.AttendanceService;
import com.example.attendancesystem.service.BaseDataService;
import com.example.attendancesystem.service.BusinessTripService;
import com.example.attendancesystem.service.EmployeeService;
import com.example.attendancesystem.service.LeaveService;
import com.example.attendancesystem.service.OvertimeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Collections;

@Controller
@RequestMapping("/admin")
public class AdminSearchController {

    private final BaseDataService baseDataService;
    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;
    private final LeaveService leaveService;
    private final OvertimeService overtimeService;
    private final BusinessTripService businessTripService;

    public AdminSearchController(BaseDataService baseDataService, EmployeeService employeeService,
                                 AttendanceService attendanceService, LeaveService leaveService,
                                 OvertimeService overtimeService, BusinessTripService businessTripService) {
        this.baseDataService = baseDataService;
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
        this.leaveService = leaveService;
        this.overtimeService = overtimeService;
        this.businessTripService = businessTripService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String employeeNo,
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false) Long departmentId,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                         Model model) {
        try {
            model.addAttribute("departments", baseDataService.departments());
            model.addAttribute("employees", employeeService.list(employeeNo, name, departmentId, 1, 1000));
            model.addAttribute("attendanceRecords", attendanceService.list(employeeNo, name, departmentId, startDate, endDate));
            model.addAttribute("leaveRecords", leaveService.list(employeeNo, name, departmentId, startDate, endDate));
            model.addAttribute("overtimeRecords", overtimeService.list(employeeNo, name, departmentId, startDate, endDate));
            model.addAttribute("tripRecords", businessTripService.list(employeeNo, name, departmentId, startDate, endDate));
            model.addAttribute("employeeNo", employeeNo);
            model.addAttribute("name", name);
            model.addAttribute("departmentId", departmentId);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage() == null ? "查询失败，请检查条件后重试" : e.getMessage());
            model.addAttribute("departments", Collections.emptyList());
            model.addAttribute("employees", Collections.emptyList());
            model.addAttribute("attendanceRecords", Collections.emptyList());
            model.addAttribute("leaveRecords", Collections.emptyList());
            model.addAttribute("overtimeRecords", Collections.emptyList());
            model.addAttribute("tripRecords", Collections.emptyList());
        }
        return "admin/search";
    }
}
