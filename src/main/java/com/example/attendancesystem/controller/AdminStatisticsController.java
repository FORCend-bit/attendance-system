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
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
public class AdminStatisticsController {

    private static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern("yyyy-MM");

    private final StatisticsService statisticsService;

    public AdminStatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/statistics")
    public String statistics(@RequestParam(required = false) String yearMonth,
                             @RequestParam(required = false) Integer year,
                             @RequestParam(required = false) Integer month,
                             Model model) {
        String selectedMonth = resolveMonth(yearMonth, year, month);
        try {
            model.addAttribute("yearMonth", selectedMonth);
            model.addAttribute("selectedYear", Integer.parseInt(selectedMonth.substring(0, 4)));
            model.addAttribute("selectedMonth", Integer.parseInt(selectedMonth.substring(5, 7)));
            model.addAttribute("years", IntStream.rangeClosed(LocalDate.now().getYear() - 3, LocalDate.now().getYear() + 1).boxed().toList());
            model.addAttribute("months", IntStream.rangeClosed(1, 12).boxed().toList());
            model.addAttribute("rows", statisticsService.monthlyByDepartment(selectedMonth));
        } catch (Exception e) {
            model.addAttribute("yearMonth", selectedMonth);
            model.addAttribute("selectedYear", LocalDate.now().getYear());
            model.addAttribute("selectedMonth", LocalDate.now().getMonthValue());
            model.addAttribute("years", IntStream.rangeClosed(LocalDate.now().getYear() - 3, LocalDate.now().getYear() + 1).boxed().toList());
            model.addAttribute("months", IntStream.rangeClosed(1, 12).boxed().toList());
            model.addAttribute("rows", Collections.emptyList());
            model.addAttribute("error", e.getMessage() == null ? "\u7edf\u8ba1\u5931\u8d25\uff0c\u8bf7\u68c0\u67e5\u6570\u636e\u5e93\u6570\u636e" : e.getMessage());
        }
        return "admin/statistics";
    }

    private String resolveMonth(String yearMonth, Integer year, Integer month) {
        if (year != null && month != null && month >= 1 && month <= 12) {
            return String.format("%04d-%02d", year, month);
        }
        if (yearMonth != null && yearMonth.matches("\\d{4}-\\d{2}")) {
            return yearMonth;
        }
        return LocalDate.now().format(YEAR_MONTH);
    }
}
