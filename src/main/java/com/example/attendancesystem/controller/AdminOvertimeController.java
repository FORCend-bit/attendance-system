package com.example.attendancesystem.controller;

import com.example.attendancesystem.entity.OvertimeRecord;
import com.example.attendancesystem.service.EmployeeService;
import com.example.attendancesystem.service.OvertimeService;
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
@RequestMapping("/admin/overtime")
public class AdminOvertimeController {

    private final OvertimeService overtimeService;
    private final EmployeeService employeeService;

    public AdminOvertimeController(OvertimeService overtimeService, EmployeeService employeeService) {
        this.overtimeService = overtimeService;
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
            model.addAttribute("records", overtimeService.list(employeeNo, name, departmentId, startDate, endDate));
            OvertimeRecord record = editId == null ? new OvertimeRecord() : overtimeService.findById(editId);
            model.addAttribute("record", record == null ? new OvertimeRecord() : record);
            model.addAttribute("employees", employeeService.list(null, null, null, 1, 1000));
        } catch (Exception e) {
            model.addAttribute("error", friendly(e));
            model.addAttribute("records", java.util.Collections.emptyList());
            model.addAttribute("record", new OvertimeRecord());
            model.addAttribute("employees", java.util.Collections.emptyList());
        }
        return "admin/overtime/list";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute OvertimeRecord record, RedirectAttributes redirectAttributes) {
        try {
            if (record.getId() == null) {
                overtimeService.save(record);
                redirectAttributes.addFlashAttribute("message", "加班记录已新增");
            } else {
                overtimeService.update(record);
                redirectAttributes.addFlashAttribute("message", "加班记录已保存");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", friendly(e));
        }
        return "redirect:/admin/overtime/list";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            overtimeService.delete(id);
            redirectAttributes.addFlashAttribute("message", "加班记录已逻辑删除");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", friendly(e));
        }
        return "redirect:/admin/overtime/list";
    }

    private String friendly(Exception e) {
        return e.getMessage() == null ? "操作失败，请检查数据后重试" : e.getMessage();
    }
}
