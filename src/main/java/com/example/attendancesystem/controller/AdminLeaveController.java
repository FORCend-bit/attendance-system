package com.example.attendancesystem.controller;

import com.example.attendancesystem.entity.LeaveRecord;
import com.example.attendancesystem.service.EmployeeService;
import com.example.attendancesystem.service.LeaveService;
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
@RequestMapping("/admin/leave")
public class AdminLeaveController {

    private final LeaveService leaveService;
    private final EmployeeService employeeService;

    public AdminLeaveController(LeaveService leaveService, EmployeeService employeeService) {
        this.leaveService = leaveService;
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
            model.addAttribute("records", leaveService.list(employeeNo, name, departmentId, startDate, endDate));
            LeaveRecord record = editId == null ? new LeaveRecord() : leaveService.findById(editId);
            model.addAttribute("record", record == null ? new LeaveRecord() : record);
            model.addAttribute("employees", employeeService.list(null, null, null, 1, 1000));
        } catch (Exception e) {
            model.addAttribute("error", friendly(e));
            model.addAttribute("records", java.util.Collections.emptyList());
            model.addAttribute("record", new LeaveRecord());
            model.addAttribute("employees", java.util.Collections.emptyList());
        }
        return "admin/leave/list";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute LeaveRecord record, RedirectAttributes redirectAttributes) {
        try {
            if (record.getId() == null) {
                leaveService.save(record);
                redirectAttributes.addFlashAttribute("message", "请假记录已新增");
            } else {
                leaveService.update(record);
                redirectAttributes.addFlashAttribute("message", "请假记录已保存");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", friendly(e));
        }
        return "redirect:/admin/leave/list";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            leaveService.delete(id);
            redirectAttributes.addFlashAttribute("message", "请假记录已取消");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", friendly(e));
        }
        return "redirect:/admin/leave/list";
    }

    private String friendly(Exception e) {
        return e.getMessage() == null ? "操作失败，请检查数据后重试" : e.getMessage();
    }
}
