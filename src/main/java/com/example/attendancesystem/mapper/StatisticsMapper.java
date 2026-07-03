package com.example.attendancesystem.mapper;

import com.example.attendancesystem.vo.MonthlyStatisticsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StatisticsMapper {

    List<MonthlyStatisticsVO> monthlyByDepartment(@Param("yearMonth") String yearMonth);
}
