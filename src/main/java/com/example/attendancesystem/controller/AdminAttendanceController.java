package com.example.attendancesystem.controller;

import com.example.attendancesystem.entity.AttendanceRecord;
import com.example.attendancesystem.service.AttendanceService;
import com.example.attendancesystem.service.EmployeeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/attendance")
public class AdminAttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeService employeeService;

    public AdminAttendanceController(AttendanceService attendanceService, EmployeeService employeeService) {
        this.attendanceService = attendanceService;
        this.employeeService = employeeService;
    }

    @GetMapping("/list")
    public String list(@RequestParam(required = false) String employeeNo,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) Long departmentId,
                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                       @RequestParam(required = false) Long editId,
                       Model model) {
        try {
            model.addAttribute("records", attendanceService.list(employeeNo, name, departmentId, startDate, endDate));
            AttendanceRecord record = editId == null ? new AttendanceRecord() : attendanceService.findById(editId);
            model.addAttribute("record", record == null ? new AttendanceRecord() : record);
            model.addAttribute("employees", employeeService.list(null, null, null, 1, 1000));
            model.addAttribute("employeeNo", employeeNo);
            model.addAttribute("name", name);
            model.addAttribute("departmentId", departmentId);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
        } catch (Exception e) {
            model.addAttribute("error", friendly(e));
            model.addAttribute("records", java.util.Collections.emptyList());
            model.addAttribute("record", new AttendanceRecord());
            model.addAttribute("employees", java.util.Collections.emptyList());
        }
        return "admin/attendance/list";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute AttendanceRecord record, RedirectAttributes redirectAttributes) {
        try {
            if (record.getId() == null) {
                attendanceService.save(record);
                redirectAttributes.addFlashAttribute("message", "出勤记录已新增");
            } else {
                attendanceService.update(record);
                redirectAttributes.addFlashAttribute("message", "出勤记录已保存");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", friendly(e));
        }
        return "redirect:/admin/attendance/list";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            attendanceService.delete(id);
            redirectAttributes.addFlashAttribute("message", "出勤记录已逻辑删除");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", friendly(e));
        }
        return "redirect:/admin/attendance/list";
    }

    private String friendly(Exception e) {
        return e.getMessage() == null ? "操作失败，请检查数据后重试" : e.getMessage();
    }
}
