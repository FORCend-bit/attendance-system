package com.example.attendancesystem.service;

import com.example.attendancesystem.vo.MonthlyStatisticsVO;

import java.util.List;

public interface StatisticsService {

    List<MonthlyStatisticsVO> monthlyByDepartment(String yearMonth);
}
