package com.example.attendancesystem.service.impl;

import com.example.attendancesystem.mapper.StatisticsMapper;
import com.example.attendancesystem.service.StatisticsService;
import com.example.attendancesystem.vo.MonthlyStatisticsVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern("yyyy-MM");

    private final StatisticsMapper statisticsMapper;

    public StatisticsServiceImpl(StatisticsMapper statisticsMapper) {
        this.statisticsMapper = statisticsMapper;
    }

    @Override
    public List<MonthlyStatisticsVO> monthlyByDepartment(String yearMonth) {
        String month = (yearMonth == null || yearMonth.isBlank()) ? LocalDate.now().format(YEAR_MONTH) : yearMonth.trim();
        List<MonthlyStatisticsVO> list = statisticsMapper.monthlyByDepartment(month);
        return list == null ? Collections.emptyList() : list;
    }
}
