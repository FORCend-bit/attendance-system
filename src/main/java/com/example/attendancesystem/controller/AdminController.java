package com.example.attendancesystem.controller;

import com.example.attendancesystem.mapper.AttendanceRecordMapper;
import com.example.attendancesystem.mapper.EmployeeMapper;
import com.example.attendancesystem.mapper.LeaveRecordMapper;
import com.example.attendancesystem.mapper.OvertimeRecordMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern("yyyy-MM");

    private final EmployeeMapper employeeMapper;
    private final AttendanceRecordMapper attendanceRecordMapper;
    private final LeaveRecordMapper leaveRecordMapper;
    private final OvertimeRecordMapper overtimeRecordMapper;

    public AdminController(EmployeeMapper employeeMapper,
                           AttendanceRecordMapper attendanceRecordMapper,
                           LeaveRecordMapper leaveRecordMapper,
                           OvertimeRecordMapper overtimeRecordMapper) {
        this.employeeMapper = employeeMapper;
        this.attendanceRecordMapper = attendanceRecordMapper;
        this.leaveRecordMapper = leaveRecordMapper;
        this.overtimeRecordMapper = overtimeRecordMapper;
    }

    @GetMapping({"", "/home"})
    public String home(Model model) {
        String currentMonth = LocalDate.now().format(YEAR_MONTH);
        try {
            model.addAttribute("employeeTotal", employeeMapper.countAll());
            model.addAttribute("activeEmployeeTotal", employeeMapper.countActive());
            model.addAttribute("resignedEmployeeTotal", employeeMapper.countResigned());
            model.addAttribute("monthLateCount", attendanceRecordMapper.countLateInMonth(currentMonth));
            model.addAttribute("monthLeaveDays", safeDecimal(leaveRecordMapper.sumDaysInMonth(currentMonth)));
            model.addAttribute("monthOvertimeHours", safeDecimal(overtimeRecordMapper.sumHoursInMonth(currentMonth)));
            model.addAttribute("currentMonth", currentMonth);
        } catch (Exception e) {
            model.addAttribute("employeeTotal", 0);
            model.addAttribute("activeEmployeeTotal", 0);
            model.addAttribute("resignedEmployeeTotal", 0);
            model.addAttribute("monthLateCount", 0);
            model.addAttribute("monthLeaveDays", BigDecimal.ZERO);
            model.addAttribute("monthOvertimeHours", BigDecimal.ZERO);
            model.addAttribute("currentMonth", currentMonth);
            model.addAttribute("error", e.getMessage() == null ? "\u9996\u9875\u7edf\u8ba1\u52a0\u8f7d\u5931\u8d25\uff0c\u8bf7\u68c0\u67e5\u6570\u636e\u5e93" : e.getMessage());
        }
        return "admin/home";
    }

    private BigDecimal safeDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
