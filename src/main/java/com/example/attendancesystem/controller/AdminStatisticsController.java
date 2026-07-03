package com.example.attendancesystem.controller;

import com.example.attendancesystem.service.StatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Controller
@RequestMapping("/admin")
public class AdminStatisticsController {

    private static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern("yyyy-MM");

    private final StatisticsService statisticsService;

    public AdminStatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/statistics")
    public String statistics(@RequestParam(required = false) String yearMonth, Model model) {
        String month = (yearMonth == null || yearMonth.isBlank()) ? LocalDate.now().format(YEAR_MONTH) : yearMonth.trim();
        try {
            model.addAttribute("yearMonth", month);
            model.addAttribute("rows", statisticsService.monthlyByDepartment(month));
        } catch (Exception e) {
            model.addAttribute("yearMonth", month);
            model.addAttribute("rows", Collections.emptyList());
            model.addAttribute("error", e.getMessage() == null ? "统计失败，请检查数据库数据" : e.getMessage());
        }
        return "admin/statistics";
    }
}
